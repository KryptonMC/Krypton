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

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.config.category.ForwardingMode
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.handshake.PacketInHandshake
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect
import org.kryptonmc.krypton.network.Session
import org.kryptonmc.krypton.network.PacketState
import org.kryptonmc.krypton.network.data.LegacyForwardedData
import org.kryptonmc.krypton.util.logger

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
        if (packet !is PacketInHandshake) return // Ignore if not a handshake packet

        // This split here is checking for a null splitted list of strings, which will be sent by BungeeCord
        // (and Velocity pre-1.13) as part of their legacy forwarding mechanisms.
        if (packet.address.split('\u0000').size > 1 && server.config.proxy.mode != ForwardingMode.LEGACY) {
            disconnect(Messages.BUNGEE.NOTIFY())
            return
        }

        if (server.config.proxy.mode == ForwardingMode.LEGACY && packet.nextState == PacketState.LOGIN) {
            val data = try {
                LegacyForwardedData.parse(packet.address)
            } catch (exception: Exception) {
                disconnect(Messages.BUNGEE.FAIL_DECODE())
                Messages.BUNGEE.FAIL_DECODE_ERROR.error(LOGGER, exception)
                return
            }

            if (data != null) {
                LOGGER.debug("Detected BungeeCord login for ${data.uuid}")
                handleStateChange(packet, packet.nextState, data)
            } else {
                // If the data was null then we weren't sent what we needed
                disconnect(Messages.BUNGEE.DIRECT())
                Messages.BUNGEE.DIRECT_WARN.warn(LOGGER, packet.address)
                return
            }
        }

        handleStateChange(packet, packet.nextState)
    }

    private fun handleStateChange(packet: PacketInHandshake, state: PacketState, data: LegacyForwardedData? = null) = when (state) {
        PacketState.LOGIN -> handleLoginRequest(packet, data)
        PacketState.STATUS -> handleStatusRequest()
        else -> throw UnsupportedOperationException("Invalid next login state $state sent in handshake!")
    }

    private fun handleLoginRequest(packet: PacketInHandshake, data: LegacyForwardedData? = null) {
        session.currentState = PacketState.LOGIN
        // This method of determining what to send is from vanilla Minecraft.
        if (packet.protocol != KryptonPlatform.protocolVersion) {
            val key = when {
                packet.protocol < KryptonPlatform.protocolVersion -> "multiplayer.disconnect.outdated_client"
                packet.protocol > KryptonPlatform.protocolVersion -> "multiplayer.disconnect.outdated_server"
                else -> "multiplayer.disconnect.incompatible" // This should be impossible
            }
            val reason = Component.translatable(key, text(KryptonPlatform.minecraftVersion))
            disconnect(reason)
            return
        }

        if (server.playerManager.players.size >= server.config.status.maxPlayers) {
            disconnect(Component.translatable("multiplayer.disconnect.server_full"))
            return
        }
        session.handler = LoginHandler(server, session, data)
    }

    private fun handleStatusRequest() {
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
