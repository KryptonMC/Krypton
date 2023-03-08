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
package org.kryptonmc.krypton.network

import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.FramedPacket
import org.kryptonmc.krypton.packet.Packet
import java.util.function.Predicate

/**
 * Utility for sending grouped packets to players.
 *
 * Grouped packets are packets that are fully encoded, framed, and compressed once, and sent
 * to multiple players, which is much faster than encoding, framing, and especially
 * compressing the packet individually for each player.
 */
object PacketGrouping {

    @JvmStatic
    fun sendGroupedPacket(players: Collection<KryptonPlayer>, packet: Packet) {
        sendGroupedPacket(players, packet, null)
    }

    @JvmStatic
    fun sendGroupedPacket(players: Collection<KryptonPlayer>, packet: Packet, predicate: Predicate<KryptonPlayer>?) {
        if (players.isEmpty()) {
            // Fast exit if there are no players to send to. This avoids encoding the packet entirely.
            return
        }

        val finalBuffer = PacketFraming.frame(packet)
        val framedPacket = FramedPacket(finalBuffer)
        players.forEach {
            if (!it.isOnline()) return@forEach
            if (predicate == null || predicate.test(it)) it.connection.write(framedPacket)
        }
        finalBuffer.release()
    }
}
