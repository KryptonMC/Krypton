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
package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.chunk.BiomeContainer
import org.kryptonmc.krypton.util.IntBiMap
import org.kryptonmc.krypton.util.ceillog2
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.world.HeightAccessor
import org.kryptonmc.krypton.world.biome.NoiseBiomeSource
import org.kryptonmc.krypton.world.biome.gen.BiomeGenerator

class KryptonBiomeContainer private constructor(
    private val biomeRegistry: IntBiMap<Biome>,
    heightAccessor: HeightAccessor,
    @get:JvmName("biomes") override val biomes: Array<Biome>
) : BiomeContainer, NoiseBiomeSource {

    private val quartMinY = heightAccessor.minimumBuildHeight shr 2
    private val quartHeight = (heightAccessor.height shr 2) - 1

    constructor(
        biomeRegistry: IntBiMap<Biome>,
        heightAccessor: HeightAccessor,
        position: ChunkPosition,
        generator: BiomeGenerator,
        biomeIds: IntArray? = null
    ) : this(
        biomeRegistry,
        heightAccessor,
        Array((1 shl WIDTH_BITS + WIDTH_BITS) * heightAccessor.height.ceilDiv(4)) {
            if (biomeIds != null && it < biomeIds.size) {
                biomeRegistry[biomeIds[it]] ?: generator.generateForIndex(
                    (position.x shl 4) shr 2,
                    heightAccessor.minimumBuildHeight shr 2,
                    (position.z shl 4) shr 2,
                    it
                )
            } else {
                generator.generateForIndex(
                    (position.x shl 4) shr 2,
                    heightAccessor.minimumBuildHeight shr 2,
                    (position.z shl 4) shr 2,
                    it
                )
            }
        }
    )

    fun write() = IntArray(biomes.size) { biomeRegistry.idOf(biomes[it]) }

    override fun get(x: Int, y: Int, z: Int): Biome {
        val offX = x and HORIZONTAL_MASK
        val offY = (y - quartMinY).clamp(0, quartHeight)
        val offZ = z and HORIZONTAL_MASK
        return biomes[offY shl WIDTH_BITS + WIDTH_BITS or (offZ shl WIDTH_BITS) or offX]
    }

    override fun set(x: Int, y: Int, z: Int, biome: Biome) {
        val offX = x and HORIZONTAL_MASK
        val offY = (y - quartMinY).clamp(0, quartHeight)
        val offZ = z and HORIZONTAL_MASK
        biomes[offY shl WIDTH_BITS + WIDTH_BITS or (offZ shl WIDTH_BITS) or offX] = biome
    }

    companion object {

        private val WIDTH_BITS = 16.ceillog2() - 2
        private val HORIZONTAL_MASK = (1 shl WIDTH_BITS) - 1

        private fun Int.ceilDiv(other: Int) = (this + other - 1) / other

        private fun BiomeGenerator.generateForIndex(x: Int, y: Int, z: Int, index: Int): Biome {
            val offX = index and HORIZONTAL_MASK
            val offY = index shr WIDTH_BITS + WIDTH_BITS
            val offZ = index shr WIDTH_BITS and HORIZONTAL_MASK
            return get(x + offX, y + offY, z + offZ)
        }
    }
}
