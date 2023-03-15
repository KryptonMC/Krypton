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
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.packet.`in`.status.PacketInPingRequest
import org.kryptonmc.krypton.packet.out.status.PacketOutPingResponse
import org.kryptonmc.krypton.packet.out.status.PacketOutStatusResponse
import org.kryptonmc.krypton.network.NioConnection
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect

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
class StatusPacketHandler(private val server: KryptonServer, override val connection: NioConnection) : PacketHandler {

    private var requestedStatus = false

    fun handleStatusRequest() {
        if (requestedStatus) {
            disconnect()
            return
        }
        requestedStatus = true
        connection.send(PacketOutStatusResponse.create(server.statusManager.status()))
    }

    fun handlePing(packet: PacketInPingRequest) {
        connection.send(PacketOutPingResponse(packet.payload))
        disconnect()
    }

    private fun disconnect() {
        connection.send(PacketOutLoginDisconnect(REQUEST_HANDLED))
        connection.disconnect(REQUEST_HANDLED)
    }

    companion object {

        private val REQUEST_HANDLED = Component.translatable("multiplayer.status.request_handled")
    }
}
