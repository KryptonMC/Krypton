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

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.status.PacketInPing
import org.kryptonmc.krypton.packet.`in`.status.PacketInStatusRequest
import org.kryptonmc.krypton.packet.out.status.PacketOutPong
import org.kryptonmc.krypton.packet.out.status.PacketOutStatusResponse
import org.kryptonmc.krypton.network.SessionHandler

/**
 * Handles all inbound packets in the
 * [Status][org.kryptonmc.krypton.packet.PacketState.STATUS] state.
 *
 * There are two packets in this state that we handle:
 * - [Status request][org.kryptonmc.krypton.packet. in.status.PacketInStatusRequest] -
 *   sent by the client to request status information
 * - [Ping][org.kryptonmc.krypton.packet. in.status.PacketInPing] -
 *   pings the server (to calculate latency on its end)
 */
class StatusHandler(
    override val server: KryptonServer,
    override val session: SessionHandler
) : PacketHandler {

    override fun handle(packet: Packet) = when (packet) {
        is PacketInStatusRequest -> handleStatusRequest()
        is PacketInPing -> handlePing(packet)
        else -> Unit
    }

    private fun handleStatusRequest() {
        session.send(PacketOutStatusResponse(server.playerManager.status))
    }

    private fun handlePing(packet: PacketInPing) {
        session.send(PacketOutPong(packet.payload))
    }
}
