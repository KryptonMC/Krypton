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
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.config.category.ProxyCategory
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.network.NettyConnection
import org.kryptonmc.krypton.network.data.ForwardedData
import org.kryptonmc.krypton.network.data.LegacyForwardedData
import org.kryptonmc.krypton.network.data.TCPShieldForwardedData
import org.kryptonmc.krypton.packet.PacketState
import org.kryptonmc.krypton.packet.`in`.handshake.PacketInHandshake

/**
 * Handles all inbound packets in the [Handshake][PacketState.HANDSHAKE] state.
 */
class HandshakeHandler(private val server: KryptonServer, override val connection: NettyConnection) : PacketHandler {

    fun handleHandshake(packet: PacketInHandshake) {
        // This method of determining what to send is from vanilla Minecraft.
        // We do this first so that we don't have to deal with legacy clients.
        if (packet.protocol != KryptonPlatform.protocolVersion) {
            val translation = when {
                packet.protocol < KryptonPlatform.protocolVersion -> Messages.Disconnect.OUTDATED_CLIENT
                packet.protocol > KryptonPlatform.protocolVersion -> Messages.Disconnect.OUTDATED_SERVER
                else -> Messages.Disconnect.INCOMPATIBLE // This should be impossible
            }
            connection.disconnect(translation.build(KryptonPlatform.minecraftVersion))
            return
        }

        // We do this early too to avoid even having to check proxy data if the server is full.
        if (server.playerManager.players.size >= server.config.status.maxPlayers) {
            connection.disconnect(Messages.Disconnect.SERVER_FULL.build())
            return
        }

        // This split here is checking for a null split list of strings, which will be sent by BungeeCord
        // (and can also be sent by Velocity) as part of their legacy forwarding mechanisms.
        if (packet.address.contains('\u0000') && server.config.proxy.mode != ProxyCategory.Mode.LEGACY) {
            LOGGER.error("User attempted legacy forwarded connection (most likely from a proxy such as BungeeCord " +
                    "or Velocity), but this server is not configured to use legacy forwarding!")
            LOGGER.info("If you wish to enable legacy forwarding, please do so in the configuration file by setting \"mode\" to \"LEGACY\" " +
                    "under the \"proxy\" section.")
            connection.disconnect(LEGACY_FORWARDING_NOT_ENABLED)
            return
        }
        // This split here is checking for a triple slash split list of strings, which will be sent by TCPShield
        // as part of its real IP forwarding mechanism.
        if (packet.address.contains("///") && server.config.proxy.mode != ProxyCategory.Mode.TCPSHIELD) {
            LOGGER.error("User attempted TCPShield forwarded connection, but this server is not configured to use TCPShield forwarding!")
            LOGGER.info("If you wish to enable TCPShield forwarding, please do so in the configuration file by setting \"mode\" to \"TCPSHIELD\" " +
                    "under the \"proxy\" section.")
            connection.disconnect(TCPSHIELD_FORWARDING_NOT_ENABLED)
            return
        }

        if (server.config.proxy.mode == ProxyCategory.Mode.LEGACY && packet.nextState == PacketState.LOGIN) {
            val data = try {
                LegacyForwardedData.parse(packet.address)
            } catch (exception: Exception) {
                connection.disconnect(FAILED_LEGACY_DECODE)
                LOGGER.error("Failed to decode legacy forwarded handshake data!", exception)
                return
            }

            if (data != null) {
                LOGGER.debug("Detected Legacy forwarded login for ${data.uuid}")
                handleStateChange(packet.nextState, data)
            } else {
                // If the data was null then we weren't sent what we needed
                connection.disconnect(NO_DIRECT_CONNECT)
                LOGGER.warn("Attempted direct connection from ${connection.connectAddress()} when legacy forwarding is enabled!")
                return
            }
        }
        if (server.config.proxy.mode == ProxyCategory.Mode.TCPSHIELD && packet.nextState == PacketState.LOGIN) {
            val data = try {
                TCPShieldForwardedData.parse(packet.address)
            } catch (exception: Exception) {
                connection.disconnect(FAILED_TCPSHIELD_DECODE)
                LOGGER.error("Failed to decode TCPShield forwarded handshake data!", exception)
                return
            }

            if (data != null) {
                LOGGER.debug("Detected TCPShield forwarded login for ${data.uuid}")
                handleStateChange(packet.nextState, data)
            } else {
                // If the data was null then we weren't sent what we needed
                connection.disconnect(NO_DIRECT_CONNECT)
                LOGGER.warn("Attempted direct connection from ${connection.connectAddress()} when TCPShield forwarding is enabled!")
                return
            }
        }

        handleStateChange(packet.nextState)
    }

    private fun handleStateChange(state: PacketState, data: ForwardedData? = null) {
        when (state) {
            PacketState.LOGIN -> connection.changeState(PacketState.LOGIN, LoginHandler(server, connection, data))
            PacketState.STATUS -> connection.changeState(PacketState.STATUS, StatusHandler(server, connection))
            else -> throw UnsupportedOperationException("Invalid next login state $state sent in handshake!")
        }
    }

    companion object {

        private val LOGGER = LogManager.getLogger()

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
