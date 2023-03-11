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
package org.kryptonmc.krypton.testutil

import org.kryptonmc.krypton.network.NetworkConnection
import org.kryptonmc.krypton.packet.GenericPacket
import org.kryptonmc.krypton.packet.Packet
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.util.ArrayDeque
import kotlin.test.assertTrue

class TestConnection : NetworkConnection {

    private val sentPackets = ArrayDeque<GenericPacket>()

    override fun connectAddress(): SocketAddress = InetSocketAddress(InetAddress.getLocalHost(), 25565)

    override fun latency(): Int = 0

    override fun send(packet: Packet) {
        write(packet)
    }

    override fun write(packet: GenericPacket) {
        sentPackets.addFirst(packet)
    }

    fun popPacket(): GenericPacket = sentPackets.removeFirst()

    fun assertNoPackets() {
        assertTrue(sentPackets.isEmpty(), "Expected no packets to be sent, but ${sentPackets.size} were!")
    }
}
