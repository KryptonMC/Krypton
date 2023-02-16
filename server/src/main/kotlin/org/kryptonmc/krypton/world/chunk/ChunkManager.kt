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
package org.kryptonmc.krypton.world.chunk

import org.apache.logging.log4j.LogManager
import java.util.concurrent.ConcurrentHashMap
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.coordinate.SectionPos
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.region.RegionFileManager
import space.vectrix.flare.fastutil.Long2ObjectSyncMap
import java.util.function.Consumer
import java.util.function.LongFunction

class ChunkManager(private val world: KryptonWorld) : AutoCloseable {

    private val chunkMap = Long2ObjectSyncMap.hashmap<KryptonChunk>()
    private val playersByChunk = Long2ObjectSyncMap.hashmap<MutableSet<KryptonPlayer>>()
    private val regionFileManager = RegionFileManager(world.folder.resolve("region"), world.server.config.advanced.synchronizeChunkWrites)

    fun chunks(): Collection<KryptonChunk> = chunkMap.values

    fun getChunk(x: Int, z: Int): KryptonChunk? = chunkMap.get(ChunkPos.pack(x, z))

    fun getChunk(position: ChunkPos): KryptonChunk? = chunkMap.get(position.pack())

    fun loadStartingArea(centerX: Int, centerZ: Int, onLoad: Consumer<KryptonChunk>) {
        for (i in 0 until STARTING_AREA_SIZE) {
            val pos = Maths.chunkInSpiral(i, centerX, centerZ)
            val chunk = loadChunk(pos)
            if (chunk != null) onLoad.accept(chunk)
        }
    }

    fun updatePlayerPosition(player: KryptonPlayer, oldPos: ChunkPos, newPos: ChunkPos, viewDistance: Int) {
        val chunksInRange = (viewDistance * 2 + 1) * (viewDistance * 2 + 1)
        for (i in 0 until chunksInRange) {
            val pos = Maths.chunkInSpiral(i, newPos.x, newPos.z)
            if (getChunk(pos) != null) continue
            loadChunk(pos)
        }

        if (oldPos == newPos) {
            if (playersByChunk.containsKey(newPos.pack())) return
            playersByChunk.computeIfAbsent(newPos.pack(), LongFunction { ConcurrentHashMap.newKeySet() }).add(player)
            return
        }

        val oldSet = playersByChunk.get(oldPos.pack())
        if (oldSet != null) {
            oldSet.remove(player)
            if (oldSet.isEmpty()) playersByChunk.remove(oldPos.pack())
        }
        playersByChunk.computeIfAbsent(newPos.pack(), LongFunction { ConcurrentHashMap.newKeySet() }).add(player)
    }

    fun removePlayer(player: KryptonPlayer) {
        val viewDistance = world.server.config.world.viewDistance
        val chunkPos = ChunkPos(SectionPos.blockToSection(player.position.x), SectionPos.blockToSection(player.position.z))
        val packedPos = chunkPos.pack()

        val chunksInRange = (viewDistance * 2 + 1) * (viewDistance * 2 + 1)
        for (i in 0 until chunksInRange) {
            val pos = Maths.chunkInSpiral(i, chunkPos.x, chunkPos.z)
            if (getChunk(pos) == null) continue // Not loaded, no need to unload it

            val playerSet = playersByChunk.get(pos.pack()) ?: continue
            if (playerSet.isEmpty()) {
                // Since there's no players in this chunk, we're safe to unload it
                unloadChunk(chunkPos.x, chunkPos.z)
            }
        }

        val playerSet = playersByChunk.get(packedPos)
        if (playerSet == null) {
            LOGGER.warn("Chunk at $chunkPos, which disconnecting player ${player.name} is in, does not have any players registered in it!")
            return
        }

        playerSet.remove(player)
        if (playerSet.isEmpty()) {
            playersByChunk.remove(packedPos)
            // This will likely have been skipped earlier in the main loop, so we'll just make sure this chunk gets unloaded if it needs to be.
            unloadChunk(chunkPos.x, chunkPos.z)
        }
    }

    private fun getPlayersByChunk(position: Long): Set<KryptonPlayer> = playersByChunk.getOrDefault(position, emptySet())

    fun loadChunk(pos: ChunkPos): KryptonChunk? {
        val packed = pos.pack()
        if (chunkMap.containsKey(packed)) return chunkMap.get(packed)!!

        val nbt = regionFileManager.read(pos.x, pos.z) ?: return null
        val chunk = ChunkSerialization.read(world, pos, nbt)

        chunkMap.put(packed, chunk)
        world.entityManager.loadAllInChunk(chunk)
        return chunk
    }

    fun unloadChunk(x: Int, z: Int) {
        val loaded = getChunk(x, z) ?: return
        saveChunk(loaded)
        chunkMap.remove(loaded.position.pack())
    }

    fun saveAllChunks(flush: Boolean) {
        chunkMap.values.forEach(::saveChunk)
        if (flush) regionFileManager.flush()
    }

    private fun saveChunk(chunk: KryptonChunk) {
        val lastUpdate = world.time
        chunk.lastUpdate = lastUpdate
        regionFileManager.write(chunk.x, chunk.z, ChunkSerialization.write(chunk))
        world.entityManager.saveAllInChunk(chunk)
    }

    @Suppress("UnusedPrivateMember")
    fun tick() {
        chunkMap.values.forEach { it.tick(getPlayersByChunk(it.position.pack()).size) }
    }

    override fun close() {
        regionFileManager.close()
    }

    companion object {

        private const val STARTING_AREA_RADIUS = 12
        private const val STARTING_AREA_SIZE = (STARTING_AREA_RADIUS * 2 + 1) * (STARTING_AREA_RADIUS * 2 + 1)
        private val LOGGER = LogManager.getLogger()
    }
}
