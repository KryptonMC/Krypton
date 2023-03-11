/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.network.handlers

import net.kyori.adventure.text.Component
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.config.category.ProxyCategory
import org.kryptonmc.krypton.locale.DisconnectMessages
import org.kryptonmc.krypton.network.NettyConnection
import org.kryptonmc.krypton.network.forwarding.LegacyForwardedData
import org.kryptonmc.krypton.network.forwarding.TCPShieldForwardedData
import org.kryptonmc.krypton.packet.PacketState
import org.kryptonmc.krypton.packet.`in`.handshake.PacketInHandshake
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect

/**
 * Handles all inbound packets in the [Handshake][PacketState.HANDSHAKE] state.
 */
class HandshakePacketHandler(private val server: KryptonServer, override val connection: NettyConnection) : PacketHandler {

    fun handleHandshake(packet: PacketInHandshake) {
        when (packet.nextState) {
            PacketState.LOGIN -> handleLoginState(packet)
            PacketState.STATUS -> handleStatusState()
            else -> LOGGER.warn("Received invalid handshake packet with state ${packet.nextState}. Ignoring...")
        }
    }

    private fun handleLoginState(packet: PacketInHandshake) {
        // Change the current state very early to prevent further packets being misinterpreted as handshake packets.
        connection.setState(PacketState.LOGIN)

        // This method of determining what to send is from vanilla Minecraft.
        // We do this first so that we don't have to deal with legacy clients.
        if (packet.protocol != KryptonPlatform.protocolVersion) {
            val message = if (packet.protocol < 754) DisconnectMessages.OUTDATED_CLIENT else DisconnectMessages.INCOMPATIBLE
            disconnect(message)
            return
        }

        // We do this early too to avoid even having to check proxy data if the server is full.
        if (server.playerManager.players().size >= server.config.status.maxPlayers) {
            disconnect(DisconnectMessages.SERVER_FULL)
            return
        }

        // This split here is checking for a null split list of strings, which will be sent by BungeeCord
        // (and can also be sent by Velocity) as part of their legacy forwarding mechanisms.
        if (packet.address.contains('\u0000') && server.config.proxy.mode != ProxyCategory.Mode.LEGACY) {
            LOGGER.error("User attempted legacy forwarded connection (most likely from a proxy such as BungeeCord " +
                    "or Velocity), but this server is not configured to use legacy forwarding!")
            LOGGER.info("If you wish to enable legacy forwarding, please do so in the configuration file by setting \"mode\" to \"LEGACY\" " +
                    "under the \"proxy\" section.")
            disconnect(DisconnectMessages.LEGACY_FORWARDING_NOT_ENABLED)
            return
        }
        // This split here is checking for a triple slash split list of strings, which will be sent by TCPShield
        // as part of its real IP forwarding mechanism.
        if (packet.address.contains("///") && server.config.proxy.mode != ProxyCategory.Mode.TCPSHIELD) {
            LOGGER.error("User attempted TCPShield forwarded connection, but this server is not configured to use TCPShield forwarding!")
            LOGGER.info("If you wish to enable TCPShield forwarding, please do so in the configuration file by setting \"mode\" to \"TCPSHIELD\" " +
                    "under the \"proxy\" section.")
            disconnect(DisconnectMessages.TCPSHIELD_FORWARDING_NOT_ENABLED)
            return
        }

        if (server.config.proxy.mode == ProxyCategory.Mode.LEGACY && packet.nextState == PacketState.LOGIN) {
            val data = try {
                LegacyForwardedData.parse(packet.address)
            } catch (exception: Exception) {
                disconnect(DisconnectMessages.FAILED_LEGACY_DECODE)
                LOGGER.error("Failed to decode legacy forwarded handshake data!", exception)
                return
            }

            if (data != null) {
                LOGGER.debug("Detected Legacy forwarded login for ${data.uuid}")
                connection.setHandler(LoginPacketHandler(server, connection, data))
            } else {
                // If the data was null then we weren't sent what we needed
                disconnect(DisconnectMessages.NO_DIRECT_CONNECT)
                LOGGER.warn("Attempted direct connection from ${connection.connectAddress()} when legacy forwarding is enabled!")
                return
            }
        }
        if (server.config.proxy.mode == ProxyCategory.Mode.TCPSHIELD && packet.nextState == PacketState.LOGIN) {
            val data = try {
                TCPShieldForwardedData.parse(packet.address)
            } catch (exception: Exception) {
                disconnect(DisconnectMessages.FAILED_TCPSHIELD_DECODE)
                LOGGER.error("Failed to decode TCPShield forwarded handshake data!", exception)
                return
            }

            if (data != null) {
                LOGGER.debug("Detected TCPShield forwarded login for ${data.uuid}")
                connection.setHandler(LoginPacketHandler(server, connection, data))
            } else {
                // If the data was null then we weren't sent what we needed
                disconnect(DisconnectMessages.NO_DIRECT_CONNECT)
                LOGGER.warn("Attempted direct connection from ${connection.connectAddress()} when TCPShield forwarding is enabled!")
                return
            }
        }
        connection.setHandler(LoginPacketHandler(server, connection, null))
    }

    private fun disconnect(reason: Component) {
        connection.writeAndDisconnect(PacketOutLoginDisconnect(reason), reason)
    }

    private fun handleStatusState() {
        connection.setState(PacketState.STATUS)
        connection.setHandler(StatusPacketHandler(server, connection))
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
    }
}
