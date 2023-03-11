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
package org.kryptonmc.krypton.entity.system

import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutSetCenterChunk
import org.kryptonmc.krypton.packet.out.play.PacketOutUnloadChunk
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.util.ChunkUtil
import java.util.concurrent.CompletableFuture

class PlayerChunkViewingSystem(private val player: KryptonPlayer) {

    private var previousCenter = ChunkPos.ZERO
    private val chunkAdder = ChunkUtil.ChunkPosConsumer { x, z ->
        val chunk = player.world.chunkManager.getChunk(x, z) ?: return@ChunkPosConsumer
        player.connection.write(chunk.cachedPacket)
    }
    private val chunkRemover = ChunkUtil.ChunkPosConsumer { x, z ->
        player.connection.send(PacketOutUnloadChunk(x, z))
    }

    fun loadInitialChunks() {
        val center = ChunkPos.forEntityPosition(player.position)
        previousCenter = center

        val range = player.server.config.world.viewDistance
        player.world.chunkManager.updateEntityPosition(player, center)

        CompletableFuture.runAsync {
            player.connection.send(PacketOutSetCenterChunk(center.x, center.z))
            ChunkUtil.forChunksInRange(center.x, center.z, range) { x, z ->
                val pos = ChunkPos(x, z)
                val chunk = player.world.chunkManager.loadChunk(pos) ?: return@forChunksInRange
                player.connection.write(chunk.cachedPacket)
            }
        }
    }

    fun updateChunks() {
        val oldCenter = previousCenter
        val newCenter = ChunkPos.forEntityPosition(player.position)
        previousCenter = newCenter

        val range = player.server.config.world.viewDistance
        player.connection.send(PacketOutSetCenterChunk(newCenter.x, newCenter.z))
        player.world.chunkManager.updatePlayerPosition(player, oldCenter, newCenter, range)
        ChunkUtil.forDifferingChunksInRange(newCenter.x, newCenter.z, oldCenter.x, oldCenter.z, range, chunkAdder, chunkRemover)
    }
}
