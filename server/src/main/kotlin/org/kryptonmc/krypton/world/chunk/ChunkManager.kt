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

import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.coordinate.SectionPos
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.tracking.EntityTypeTarget
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.region.RegionFileManager
import space.vectrix.flare.fastutil.Long2ObjectSyncMap
import java.util.function.Consumer

class ChunkManager(private val world: KryptonWorld) : AutoCloseable {

    private val chunkMap = Long2ObjectSyncMap.hashmap<KryptonChunk>()
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
        if (oldPos == newPos) return

        val chunksInRange = (viewDistance * 2 + 1) * (viewDistance * 2 + 1)
        for (i in 0 until chunksInRange) {
            val pos = Maths.chunkInSpiral(i, newPos.x, newPos.z)
            if (getChunk(pos) != null) continue
            loadChunk(pos)
        }

        updateEntityPosition(player, newPos)
    }

    fun updateEntityPosition(entity: KryptonEntity, newPos: ChunkPos) {
        val newChunk = getChunk(newPos) ?: return
        world.server.tickDispatcher().queueElementUpdate(entity, newChunk)
    }

    fun removePlayer(player: KryptonPlayer) {
        val viewDistance = world.server.config.world.viewDistance
        val chunkPos = ChunkPos(SectionPos.blockToSection(player.position.x), SectionPos.blockToSection(player.position.z))

        val chunksInRange = (viewDistance * 2 + 1) * (viewDistance * 2 + 1)
        for (i in 0 until chunksInRange) {
            val pos = Maths.chunkInSpiral(i, chunkPos.x, chunkPos.z)
            val chunk = getChunk(pos) ?: continue // Not loaded, no need to unload it
            if (chunk.players.isEmpty()) unloadChunk(chunkPos.x, chunkPos.z)
        }

        val playersInChunk = world.entityTracker.entitiesInChunkOfType(chunkPos, EntityTypeTarget.PLAYERS)
        if (playersInChunk.isEmpty()) unloadChunk(chunkPos.x, chunkPos.z)
        world.server.tickDispatcher().queueElementRemove(player)
    }

    fun loadChunk(pos: ChunkPos): KryptonChunk? {
        val packed = pos.pack()
        if (chunkMap.containsKey(packed)) return chunkMap.get(packed)!!

        val nbt = regionFileManager.read(pos.x, pos.z) ?: return null
        val chunk = ChunkSerialization.read(world, pos, nbt)

        world.server.tickDispatcher().queuePartitionLoad(chunk)
        chunkMap.put(packed, chunk)
        world.entityManager.loadAllInChunk(chunk)
        return chunk
    }

    fun unloadChunk(x: Int, z: Int) {
        val loaded = getChunk(x, z) ?: return
        saveChunk(loaded)
        chunkMap.remove(loaded.position.pack())
        world.server.tickDispatcher().queuePartitionUnload(loaded)
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

    override fun close() {
        regionFileManager.close()
    }

    companion object {

        private const val STARTING_AREA_RADIUS = 12
        private const val STARTING_AREA_SIZE = (STARTING_AREA_RADIUS * 2 + 1) * (STARTING_AREA_RADIUS * 2 + 1)
    }
}
