package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.world.KryptonWorld

interface ChunkLoader : AutoCloseable {

    fun loadChunk(world: KryptonWorld, pos: ChunkPos): KryptonChunk?

    fun loadAllEntities(chunk: KryptonChunk)

    fun saveChunk(chunk: KryptonChunk)

    fun saveAllEntities(chunk: KryptonChunk)

    override fun close() {
        // Do nothing by default - optional, only if required
    }
}
