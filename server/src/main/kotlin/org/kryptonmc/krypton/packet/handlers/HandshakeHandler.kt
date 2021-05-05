/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet.handlers

import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.extra.kotlin.translatable
import net.kyori.adventure.text.Component.text
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.krypton.api.event.events.handshake.HandshakeEvent
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.handshake.BungeeCordHandshakeData
import org.kryptonmc.krypton.packet.`in`.handshake.PacketInHandshake
import org.kryptonmc.krypton.packet.`in`.handshake.splitData
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect
import org.kryptonmc.krypton.packet.session.Session
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.util.logger
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
        server.eventBus.call(HandshakeEvent(session.channel.remoteAddress() as InetSocketAddress))

        if (packet.data.address.split('\u0000').size > 1 && !server.config.other.bungeecord) {
            disconnect("Please notify the server administrator that they are attempting to use BungeeCord IP forwarding without enabling BungeeCord support in their configuration file.")
            return
        }

        if (server.config.other.bungeecord && packet.data.nextState == PacketState.LOGIN) {
            val data = try {
                packet.data.address.splitData()
            } catch (exception: Exception) {
                disconnect("Could not decode BungeeCord handshake data! Please report this to an administrator!")
                LOGGER.error("Error decoding BungeeCord handshake data! Please report this to Krypton.", exception)
                return
            }

            if (data != null) {
                LOGGER.debug("Detected BungeeCord login for ${data.uuid}")
                changeState(packet, packet.data.nextState, data)
            } else {
                disconnect("You are unable to directly connect to this server, as it has BungeeCord enabled in the configuration.")
                LOGGER.warn("Attempted connection from ${packet.data.address} not from BungeeCord when BungeeCord compatibility enabled.")
                return
            }
        }

        changeState(packet, packet.data.nextState)
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
        if (packet.data.protocol != ServerInfo.PROTOCOL) {
            val key = when {
                packet.data.protocol < ServerInfo.PROTOCOL -> "multiplayer.disconnect.outdated_client"
                packet.data.protocol > ServerInfo.PROTOCOL -> "multiplayer.disconnect.outdated_server"
                else -> "multiplayer.disconnect.incompatible"
            }
            val reason = translatable {
                key(key)
                args(text { content(ServerInfo.VERSION) })
            }
            session.disconnect(reason)
            return
        }
        if (ServerStorage.PLAYER_COUNT.get() >= server.config.status.maxPlayers) {
            session.disconnect(translatable { key("multiplayer.disconnect.server_full") })
            return
        }
        session.handler = LoginHandler(server, server.sessionManager, session, data)
    }

    /**
     * Handles when next state is [PacketState.STATUS]
     */
    private fun handleStatus() {
        session.currentState = PacketState.STATUS
        session.handler = StatusHandler(server, session)
    }

    private fun disconnect(reason: String) {
        session.sendPacket(PacketOutLoginDisconnect(text(reason)))
        if (session.channel.isOpen) session.channel.closeFuture().syncUninterruptibly()
    }

    companion object {

        private val LOGGER = logger<HandshakeHandler>()
    }
}
