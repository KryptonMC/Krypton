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
package org.kryptonmc.krypton.world

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.registry.RegistryHolder
import org.kryptonmc.krypton.world.biome.BiomeManager
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.biome.NoiseBiomeSource
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import org.kryptonmc.krypton.world.chunk.ChunkManager
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import org.kryptonmc.krypton.world.data.WorldData
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.spongepowered.math.vector.Vector3i
import java.util.Random

interface WorldAccessor : BlockAccessor, NoiseBiomeSource {

    val server: KryptonServer
    val registryHolder: RegistryHolder
    val world: KryptonWorld
    val data: WorldData
    val dimensionType: KryptonDimensionType
    val chunkManager: ChunkManager
    val biomeManager: BiomeManager
    val random: Random
    val seed: Long

    val seaLevel: Int
    val dayTime: Long
        get() = data.dayTime
    val moonBrightness: Float
        get() = KryptonDimensionType.MOON_BRIGHTNESS_BY_PHASE[dimensionType.moonPhase(dayTime)]
    val moonPhase: Int
        get() = dimensionType.moonPhase(dayTime)
    val timeOfDay: Float
        get() = dimensionType.timeOfDay(dayTime)
    override val height: Int
        get() = dimensionType.height
    override val minimumBuildHeight: Int
        get() = dimensionType.minimumY

    fun hasChunk(x: Int, z: Int): Boolean = chunkManager[x, z] != null

    fun hasChunkAt(x: Int, z: Int) = hasChunk(x shr 4, z shr 4)

    fun hasChunkAt(position: Vector3i) = hasChunkAt(position.x(), position.z())

    fun getChunk(position: Vector3i) = getChunk(position.x() shr 4, position.z() shr 4)

    fun getChunk(x: Int, z: Int, status: ChunkStatus = ChunkStatus.FULL, shouldCreate: Boolean = true): ChunkAccessor?

    fun getHeight(type: Heightmap.Type, x: Int, z: Int): Int

    fun getBiome(x: Int, y: Int, z: Int): KryptonBiome = biomeManager[x, y, z]

    fun getUncachedNoiseBiome(x: Int, y: Int, z: Int): KryptonBiome

    override fun get(x: Int, y: Int, z: Int): KryptonBiome {
        val chunk = getChunk(x shr 2, z shr 2, ChunkStatus.BIOMES, false)
        return if (chunk?.biomes != null) chunk.biomes!![x, y, z] else getUncachedNoiseBiome(x, y, z)
    }

    fun canWrite(x: Int, y: Int, z: Int): Boolean = true
}
