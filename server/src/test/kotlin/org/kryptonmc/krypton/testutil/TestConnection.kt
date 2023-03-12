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
