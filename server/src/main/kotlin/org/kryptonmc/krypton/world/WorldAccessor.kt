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
package org.kryptonmc.krypton.world

import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeContainer
import org.kryptonmc.api.world.biome.Biomes
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.world.biome.BiomeManager
import org.kryptonmc.krypton.world.biome.NoiseBiomeSource
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import org.kryptonmc.krypton.world.chunk.ChunkManager
import org.kryptonmc.krypton.world.chunk.ChunkStatus

interface WorldAccessor : BlockAccessor, NoiseBiomeSource, BiomeContainer {

    val dimensionType: DimensionType
    val chunkManager: ChunkManager
    val biomeManager: BiomeManager

    override val height: Int
        get() = dimensionType.height
    override val minimumBuildHeight: Int
        get() = dimensionType.minimumY

    fun hasChunk(x: Int, z: Int): Boolean = chunkManager.get(x, z) != null

    fun hasChunkAt(x: Int, z: Int): Boolean = hasChunk(x shr 4, z shr 4)

    fun hasChunkAt(position: BlockPos): Boolean = hasChunkAt(position.x, position.z)

    fun getChunk(position: Vec3i): ChunkAccessor? = getChunk(position.x shr 4, position.z shr 4)

    fun getChunk(x: Int, z: Int, status: ChunkStatus = ChunkStatus.FULL, shouldCreate: Boolean = true): ChunkAccessor?

    fun getHeight(type: Heightmap.Type, x: Int, z: Int): Int

    override fun getBiome(x: Int, y: Int, z: Int): Biome = biomeManager.getBiome(x, y, z)

    override fun getBiome(position: Vec3i): Biome = getBiome(position.x, position.y, position.z)

    override fun getNoiseBiome(x: Int, y: Int, z: Int): Biome {
        val chunk = getChunk(x shr 2, z shr 2, ChunkStatus.BIOMES, false) ?: return Biomes.PLAINS
        return chunk.getNoiseBiome(x, y, z)
    }
}
