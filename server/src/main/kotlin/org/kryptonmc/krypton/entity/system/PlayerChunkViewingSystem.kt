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
