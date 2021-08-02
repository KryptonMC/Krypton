/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.world.generation

import com.google.common.hash.Hashing
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.util.floor
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.WorldAccessor
import org.kryptonmc.krypton.world.biome.BiomeManager
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import kotlin.math.abs
import kotlin.math.sqrt

class GenerationRegion(
    override val world: KryptonWorld,
    cache: MutableList<ChunkAccessor>,
    private val status: ChunkStatus,
    private val writeRadiusCutoff: Int
) : WorldAccessor {

    private val cache: MutableList<ChunkAccessor>
    val center: ChunkPosition
    private val size: Int
    private val firstPosition: ChunkPosition
    private val lastPosition: ChunkPosition
    var currentlyGenerating: (() -> String)? = null
    override val seed = world.seed
    override val random = world.random
    override val dimensionType = world.dimensionType
    override val biomeManager = BiomeManager(this, Hashing.sha256().hashLong(seed).asLong(), world.dimensionType.biomeZoomer)
    override val chunkManager = world.chunkManager
    override val data = world.data
    override val seaLevel = world.seaLevel
    override val registryHolder = world.registryHolder
    override val server = world.server

    init {
        val size = sqrt(cache.size.toDouble()).floor()
        check(size * size == cache.size) { "Cache size must be a square number!" }
        val pos = cache[cache.size / 2].position
        this.cache = cache
        center = pos
        this.size = size
        firstPosition = cache[0].position
        lastPosition = cache.last().position
    }

    override fun hasChunk(x: Int, z: Int) = x >= firstPosition.x && x <= lastPosition.x && z >= firstPosition.z && z <= lastPosition.z

    override fun getChunk(x: Int, z: Int, status: ChunkStatus, shouldCreate: Boolean): ChunkAccessor? {
        val chunk = if (hasChunk(x, z)) {
            val localX = x - firstPosition.x
            val localZ = z - firstPosition.z
            cache[localX + localZ * size].apply { if (this.status.isOrAfter(status)) return this }
        } else null
        if (!shouldCreate) return null
        LOGGER.error("Requested chunk at $x, $z is out of region bounds at ${firstPosition.x}, ${firstPosition.z} to ${lastPosition.x}, ${lastPosition.z}!")
        throw RuntimeException(if (chunk != null) {
            "Requested chunk at $x, $z has the wrong status! Expected $status, was ${chunk.status}"
        } else {
            "Requested chunk at $x, $z is out of bounds!"
        })
    }

    override fun getBlock(x: Int, y: Int, z: Int) = getChunk(x, z)?.getBlock(x, y, z) ?: Blocks.AIR

    override fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        if (!canWrite(x, y, z)) return
        getChunk(x shr 4, z shr 4)?.setBlock(x, y, z, block)
    }

    override fun getHeight(type: Heightmap.Type, x: Int, z: Int) = getChunk(x shr 4, z shr 4)!!.getHeight(type, x and 15, z and 15) + 1

    override fun getUncachedNoiseBiome(x: Int, y: Int, z: Int) = world.getUncachedNoiseBiome(x, y, z)

    override fun canWrite(x: Int, y: Int, z: Int): Boolean {
        val chunkX = x shr 4
        val chunkZ = z shr 4
        val dx = abs(center.x - chunkX)
        val dz = abs(center.z - chunkZ)
        return if (dx <= writeRadiusCutoff && dz <= writeRadiusCutoff) {
            true
        } else {
            LOGGER.error("Attempted to set block in chunk out of bounds! Chunk: ($chunkX, $chunkZ), position: ($x, $y, $z), status: $status${currentlyGenerating?.invoke()?.let { ", currently generating: $it" }}")
            false
        }
    }

    companion object {

        private val LOGGER = logger<GenerationRegion>()
    }
}
