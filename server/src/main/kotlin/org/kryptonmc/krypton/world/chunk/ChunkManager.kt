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
package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.coordinate.SectionPos
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.tracking.EntityTypeTarget
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.world.KryptonWorld
import space.vectrix.flare.fastutil.Long2ObjectSyncMap
import java.util.function.Consumer

class ChunkManager(private val world: KryptonWorld, private val chunkLoader: ChunkLoader) : AutoCloseable {

    private val chunkMap = Long2ObjectSyncMap.hashmap<KryptonChunk>()

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

        val chunk = chunkLoader.loadChunk(world, pos) ?: return null
        world.server.tickDispatcher().queuePartitionLoad(chunk)
        chunkMap.put(packed, chunk)
        chunkLoader.loadAllEntities(chunk)
        return chunk
    }

    fun unloadChunk(x: Int, z: Int) {
        val loaded = getChunk(x, z) ?: return
        saveChunk(loaded)
        chunkMap.remove(loaded.position.pack())
        world.server.tickDispatcher().queuePartitionUnload(loaded)
    }

    fun saveAllChunks() {
        chunkMap.values.forEach(::saveChunk)
    }

    private fun saveChunk(chunk: KryptonChunk) {
        chunk.lastUpdate = world.time
        chunkLoader.saveChunk(chunk)
        chunkLoader.saveAllEntities(chunk)
    }

    override fun close() {
        chunkLoader.close()
    }

    companion object {

        private const val STARTING_AREA_RADIUS = 12
        private const val STARTING_AREA_SIZE = (STARTING_AREA_RADIUS * 2 + 1) * (STARTING_AREA_RADIUS * 2 + 1)
    }
}
