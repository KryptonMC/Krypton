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

import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongArraySet
import it.unimi.dsi.fastutil.longs.LongSet
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutSetCenterChunk
import org.kryptonmc.krypton.packet.out.play.PacketOutUnloadChunk
import org.kryptonmc.krypton.coordinate.ChunkPos
import java.util.concurrent.CompletableFuture
import kotlin.math.abs

class PlayerChunkViewingSystem(private val player: KryptonPlayer) {

    private var previousCenter = ChunkPos.ZERO
    private val visibleChunks = LongArraySet()

    fun loadInitialChunks() {
        updateChunks(true)
    }

    fun updateChunks() {
        updateChunks(false)
    }

    private fun updateChunks(firstLoad: Boolean) {
        var previousChunks: LongSet? = null
        val newChunks = LongArrayList()

        val oldCenter = previousCenter
        val newCenter = ChunkPos.forEntityPosition(player.position)
        previousCenter = newCenter
        val radius = player.server.config.world.viewDistance

        if (firstLoad) {
            for (x in newCenter.x - radius..newCenter.x + radius) {
                for (z in newCenter.z - radius..newCenter.z + radius) {
                    newChunks.add(ChunkPos.pack(x, z))
                }
            }
        } else if (abs(newCenter.x - oldCenter.x) > radius || abs(newCenter.z - oldCenter.z) > radius) {
            visibleChunks.clear()
            for (x in newCenter.x - radius..newCenter.x + radius) {
                for (z in newCenter.z - radius..newCenter.z + radius) {
                    newChunks.add(ChunkPos.pack(x, z))
                }
            }
        } else if (oldCenter.x != newCenter.x || oldCenter.z != newCenter.z) {
            previousChunks = LongArraySet(visibleChunks)
            for (x in newCenter.x - radius..newCenter.x + radius) {
                for (z in newCenter.z - radius..newCenter.z + radius) {
                    val pos = ChunkPos.pack(x, z)
                    if (visibleChunks.contains(pos)) previousChunks.remove(pos) else newChunks.add(pos)
                }
            }
        } else {
            return
        }

        newChunks.sortWith { a, b ->
            var dx = 16 * a.toInt() + 8 - player.position.x
            var dz = 16 * (a shr 32).toInt() + 8 - player.position.z
            val da = dx * dx + dz * dz
            dx = 16 * b.toInt() + 8 - player.position.x
            dz = 16 * (b shr 32).toInt() + 8 - player.position.z
            val db = dx * dx + dz * dz
            da.compareTo(db)
        }

        visibleChunks.addAll(newChunks)
        val oldPosForPlayer = if (firstLoad) newCenter else oldCenter

        player.world.chunkManager.updatePlayerPosition(player, oldPosForPlayer, newCenter, radius)

        CompletableFuture.runAsync {
            player.connection.send(PacketOutSetCenterChunk(newCenter.x, newCenter.z))
            newChunks.forEach {
                val chunk = player.world.chunkManager.getChunk(ChunkPos.unpackX(it), ChunkPos.unpackZ(it)) ?: return@forEach
                player.connection.write(chunk.cachedPacket)
            }

            if (previousChunks == null) return@runAsync
            previousChunks.forEach {
                player.connection.send(PacketOutUnloadChunk(it.toInt(), (it shr 32).toInt()))
                visibleChunks.remove(it)
            }
            previousChunks.clear()
        }
    }
}
