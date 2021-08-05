/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.network.handlers

import net.kyori.adventure.extra.kotlin.translatable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.api.event.handshake.HandshakeEvent
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.config.category.ForwardingMode
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.handshake.PacketInHandshake
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect
import org.kryptonmc.krypton.network.Session
import org.kryptonmc.krypton.network.PacketState
import org.kryptonmc.krypton.util.BungeeCordHandshakeData
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.splitData
import java.net.InetSocketAddress

/**
 * Handles all inbound packets in the [Handshake][PacketState.HANDSHAKE] state. Contrary to the
 * other handlers and states, there is only one packet in this state, so this is directly handled
 * by the [handle] function, instead of delegating to another function.
 */
class HandshakeHandler(
    override val server: KryptonServer,
    override val session: Session
) : PacketHandler {

    override fun handle(packet: Packet) {
        if (packet !is PacketInHandshake) return // ignore if not a handshake packet
        server.eventManager.fireAndForget(HandshakeEvent(session.channel.remoteAddress() as InetSocketAddress))

        if (packet.address.split('\u0000').size > 1 && server.config.proxy.mode != ForwardingMode.LEGACY) {
            disconnect(Messages.BUNGEE.NOTIFY())
            return
        }

        if (server.config.proxy.mode == ForwardingMode.LEGACY && packet.nextState == PacketState.LOGIN) {
            val data = try {
                packet.address.splitData()
            } catch (exception: Exception) {
                disconnect(Messages.BUNGEE.FAIL_DECODE())
                Messages.BUNGEE.FAIL_DECODE_ERROR.error(LOGGER, exception)
                return
            }

            if (data != null) {
                LOGGER.debug("Detected BungeeCord login for ${data.uuid}")
                changeState(packet, packet.nextState, data)
            } else {
                disconnect(Messages.BUNGEE.DIRECT())
                Messages.BUNGEE.DIRECT_WARN.warn(LOGGER, packet.address)
                return
            }
        }

        changeState(packet, packet.nextState)
    }

    private fun changeState(packet: PacketInHandshake, state: PacketState, data: BungeeCordHandshakeData? = null) = when (state) {
        PacketState.LOGIN -> handleLogin(packet, data)
        PacketState.STATUS -> handleStatus()
        else -> throw UnsupportedOperationException("Invalid next state $state!")
    }

    /**
     * Handles when next state is [PacketState.LOGIN]
     */
    private fun handleLogin(packet: PacketInHandshake, data: BungeeCordHandshakeData? = null) {
        session.currentState = PacketState.LOGIN
        if (packet.protocol != KryptonPlatform.protocolVersion) {
            val key = when {
                packet.protocol < KryptonPlatform.protocolVersion -> "multiplayer.disconnect.outdated_client"
                packet.protocol > KryptonPlatform.protocolVersion -> "multiplayer.disconnect.outdated_server"
                else -> "multiplayer.disconnect.incompatible"
            }
            val reason = translatable {
                key(key)
                args(text(KryptonPlatform.minecraftVersion))
            }
            session.disconnect(reason)
            return
        }
        if (server.playerManager.players.size >= server.config.status.maxPlayers) {
            session.disconnect(translatable { key("multiplayer.disconnect.server_full") })
            return
        }
        session.handler = LoginHandler(server, session, data)
    }

    /**
     * Handles when next state is [PacketState.STATUS]
     */
    private fun handleStatus() {
        session.currentState = PacketState.STATUS
        session.handler = StatusHandler(server, session)
    }

    private fun disconnect(reason: Component) {
        session.sendPacket(PacketOutLoginDisconnect(reason))
        if (session.channel.isOpen) session.channel.closeFuture().syncUninterruptibly()
    }

    companion object {

        private val LOGGER = logger<HandshakeHandler>()
    }
}
