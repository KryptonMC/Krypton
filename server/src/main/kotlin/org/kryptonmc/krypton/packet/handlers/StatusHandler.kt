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

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.status.PacketInPing
import org.kryptonmc.krypton.packet.`in`.status.PacketInStatusRequest
import org.kryptonmc.krypton.packet.data.PlayerInfo
import org.kryptonmc.krypton.packet.data.Players
import org.kryptonmc.krypton.packet.data.ServerVersion
import org.kryptonmc.krypton.packet.data.StatusResponse
import org.kryptonmc.krypton.packet.out.status.PacketOutPong
import org.kryptonmc.krypton.packet.out.status.PacketOutStatusResponse
import org.kryptonmc.krypton.packet.session.Session
import org.kryptonmc.krypton.packet.state.PacketState

/**
 * Handles all inbound packets in the [Status][org.kryptonmc.krypton.packet.state.PacketState.STATUS] state.
 *
 * There are two packets in this state that we handle:
 * - [Status request][org.kryptonmc.krypton.packet. in.status.PacketInStatusRequest] -
 *   sent by the client to request status information
 * - [Ping][org.kryptonmc.krypton.packet. in.status.PacketInPing] -
 *   pings the server (to calculate latency on its end)
 */
class StatusHandler(
    override val server: KryptonServer,
    override val session: Session
) : PacketHandler {

    override fun handle(packet: Packet) = when (packet) {
        is PacketInStatusRequest -> handleStatusRequest()
        is PacketInPing -> handlePing(packet)
        else -> Unit
    }

    private fun handleStatusRequest() {
        val players = server.sessionManager.sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .map { PlayerInfo(it.profile.name, it.profile.uuid) }
            .toSet()

        session.sendPacket(PacketOutStatusResponse(StatusResponse(
            ServerVersion(ServerInfo.VERSION, ServerInfo.PROTOCOL),
            Players(server.status.maxPlayers, ServerStorage.PLAYER_COUNT.get(), players),
            server.status.motd
        )))
    }

    private fun handlePing(packet: PacketInPing) = session.sendPacket(PacketOutPong(packet.payload))
}
