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
package org.kryptonmc.krypton.network

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.CachedPacket
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
    fun sendGroupedPacket(server: KryptonServer, packet: Packet) {
        sendGroupedPacket(server.playerManager.players(), packet, null)
    }

    @JvmStatic
    fun sendGroupedPacket(server: KryptonServer, packet: Packet, predicate: Predicate<KryptonPlayer>) {
        sendGroupedPacket(server.playerManager.players(), packet, predicate)
    }

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

        val cachedPacket = CachedPacket { packet }
        players.forEach { player ->
            if (!player.isOnline()) return@forEach
            if (predicate == null || predicate.test(player)) player.connection.write(cachedPacket)
        }
    }
}
