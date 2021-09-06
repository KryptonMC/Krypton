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
package org.kryptonmc.krypton.world.biome

import org.kryptonmc.api.util.floor
import org.kryptonmc.krypton.world.biome.gen.BiomeZoomer
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.spongepowered.math.vector.Vector3i

class BiomeManager(
    private val source: NoiseBiomeSource,
    private val seed: Long,
    private val zoomer: BiomeZoomer
) {

    operator fun get(x: Int, y: Int, z: Int) = zoomer(seed, x, y, z, source)

    fun getNoiseBiome(x: Double, y: Double, z: Double): KryptonBiome {
        val quartX = x.floor() shr 2
        val quartY = y.floor() shr 2
        val quartZ = z.floor() shr 2
        return getNoiseBiome(quartX, quartY, quartZ)
    }

    fun getNoiseBiome(position: Vector3i): KryptonBiome {
        val quartX = position.x() shr 2
        val quartY = position.y() shr 2
        val quartZ = position.z() shr 2
        return getNoiseBiome(quartX, quartY, quartZ)
    }

    private fun getNoiseBiome(x: Int, y: Int, z: Int) = source[x, y, z]

    companion object {

        const val CENTER_QUART = 8 shr 2
    }
}
