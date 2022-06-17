/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.config.category.ForwardingMode
import org.kryptonmc.krypton.network.SessionHandler
import org.kryptonmc.krypton.network.data.ForwardedData
import org.kryptonmc.krypton.network.data.LegacyForwardedData
import org.kryptonmc.krypton.network.data.TCPShieldForwardedData
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketState
import org.kryptonmc.krypton.packet.`in`.handshake.PacketInHandshake
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect
import org.kryptonmc.krypton.util.logger

/**
 * Handles all inbound packets in the [Handshake][PacketState.HANDSHAKE] state.
 * Contrary to the other handlers and states, there is only one packet in this
 * state, so this is directly handled by the [handle] function, instead of
 * delegating to another function.
 */
class HandshakeHandler(override val server: KryptonServer, override val session: SessionHandler) : PacketHandler {

    override fun handle(packet: Packet) {
        if (packet !is PacketInHandshake) return // Ignore if not a handshake packet

        // This method of determining what to send is from vanilla Minecraft.
        // We do this first so that we don't have to deal with legacy clients.
        if (packet.protocol != KryptonPlatform.protocolVersion) {
            val key = when {
                packet.protocol < KryptonPlatform.protocolVersion -> "multiplayer.disconnect.outdated_client"
                packet.protocol > KryptonPlatform.protocolVersion -> "multiplayer.disconnect.outdated_server"
                else -> "multiplayer.disconnect.incompatible" // This should be impossible
            }
            val reason = Component.translatable(key, Component.text(KryptonPlatform.minecraftVersion))
            disconnect(reason)
            return
        }

        // We do this early too to avoid even having to check proxy data if the server is full.
        if (server.playerManager.players.size >= server.config.status.maxPlayers) {
            disconnect(Component.translatable("multiplayer.disconnect.server_full"))
            return
        }

        // This split here is checking for a null split list of strings, which will be sent by BungeeCord
        // (and can also be sent by Velocity) as part of their legacy forwarding mechanisms.
        if (packet.address.contains('\u0000') && server.config.proxy.mode != ForwardingMode.LEGACY) {
            LOGGER.error("User attempted legacy forwarded connection (most likely from a proxy such as BungeeCord " +
                    "or Velocity), but this server is not configured to use legacy forwarding!")
            LOGGER.info("If you wish to enable legacy forwarding, please do so in the configuration file by setting \"mode\" to \"LEGACY\" " +
                    "under the \"proxy\" section.")
            disconnect(LEGACY_FORWARDING_NOT_ENABLED)
            return
        }
        // This split here is checking for a triple slash split list of strings, which will be sent by TCPShield
        // as part of its real IP forwarding mechanism.
        if (packet.address.contains("///") && server.config.proxy.mode != ForwardingMode.TCPSHIELD) {
            LOGGER.error("User attempted TCPShield forwarded connection, but this server is not configured to use TCPShield forwarding!")
            LOGGER.info("If you wish to enable TCPShield forwarding, please do so in the configuration file by setting \"mode\" to \"TCPSHIELD\" " +
                    "under the \"proxy\" section.")
            disconnect(TCPSHIELD_FORWARDING_NOT_ENABLED)
            return
        }

        if (server.config.proxy.mode == ForwardingMode.LEGACY && packet.nextState == PacketState.LOGIN) {
            val data = try {
                LegacyForwardedData.parse(packet.address)
            } catch (exception: Exception) {
                disconnect(FAILED_LEGACY_DECODE)
                LOGGER.error("Failed to decode legacy forwarded handshake data!", exception)
                return
            }

            if (data != null) {
                LOGGER.debug("Detected Legacy forwarded login for ${data.uuid}")
                handleStateChange(packet.nextState, data)
            } else {
                // If the data was null then we weren't sent what we needed
                disconnect(NO_DIRECT_CONNECT)
                LOGGER.warn("Attempted direct connection from ${session.channel.remoteAddress()} when legacy forwarding is enabled!")
                return
            }
        }
        if (server.config.proxy.mode == ForwardingMode.TCPSHIELD && packet.nextState == PacketState.LOGIN) {
            val data = try {
                TCPShieldForwardedData.parse(packet.address)
            } catch (exception: Exception) {
                disconnect(FAILED_TCPSHIELD_DECODE)
                LOGGER.error("Failed to decode TCPShield forwarded handshake data!", exception)
                return
            }

            if (data != null) {
                LOGGER.debug("Detected TCPShield forwarded login")
                handleStateChange(packet.nextState, data)
            } else {
                // If the data was null then we weren't sent what we needed
                disconnect(NO_DIRECT_CONNECT)
                LOGGER.warn("Attempted direct connection from ${session.channel.remoteAddress()} when TCPShield forwarding is enabled!")
                return
            }
        }

        handleStateChange(packet.nextState)
    }

    private fun handleStateChange(state: PacketState, data: ForwardedData? = null) {
        when (state) {
            PacketState.LOGIN -> handleLoginRequest(data)
            PacketState.STATUS -> handleStatusRequest()
            else -> throw UnsupportedOperationException("Invalid next login state $state sent in handshake!")
        }
    }

    private fun handleLoginRequest(data: ForwardedData? = null) {
        session.currentState = PacketState.LOGIN
        session.handler = LoginHandler(server, session, data)
    }

    private fun handleStatusRequest() {
        session.currentState = PacketState.STATUS
        session.handler = StatusHandler(server, session)
    }

    private fun disconnect(reason: Component) {
        session.send(PacketOutLoginDisconnect(reason))
        if (session.channel.isOpen) session.channel.close().awaitUninterruptibly()
    }

    companion object {

        private val LOGGER = logger<HandshakeHandler>()

        private val LEGACY_FORWARDING_NOT_ENABLED = Component.text()
            .content("It appears that you have been forwarded using legacy forwarding by a proxy, but this server is not configured")
            .append(Component.newline())
            .append(Component.text("to support legacy forwarding. Please contact a server administrator."))
            .build()
        private val TCPSHIELD_FORWARDING_NOT_ENABLED = Component.text()
            .content("It appears that you have been forwarded from TCPShield, but this server is not configured to support TCPShield forwarding.")
            .append(Component.newline())
            .append(Component.text("Please contact a server administrator."))
            .build()
        private val FAILED_LEGACY_DECODE = Component.text("Failed to decode legacy data! Please report this to an administrator!")
        private val FAILED_TCPSHIELD_DECODE = Component.text("Failed to decode TCPShield data! Please report this to an administrator!")
        private val NO_DIRECT_CONNECT = Component.text("This server cannot be direct connected to whilst it has forwarding enabled.")
    }
}
