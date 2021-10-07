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
package org.kryptonmc.krypton.world.biome.layer

import org.kryptonmc.krypton.world.biome.context.Context
import org.kryptonmc.krypton.world.biome.layer.traits.C0Transformer

class BiomeInitLayer private constructor(private val warmBiomes: IntArray) : C0Transformer {

    override fun invoke(context: Context, value: Int): Int {
        val shifted = (value and 3840) shr 8
        var temp = value
        temp = temp and -3841
        if (temp.isOcean() || temp == 14) return temp
        return when (temp) {
            1 -> if (shifted > 0) {
                if (context.nextRandom(3) == 0) 39 else 38
            } else {
                warmBiomes[context.nextRandom(warmBiomes.size)]
            }
            2 -> if (shifted > 0) 21 else MEDIUM_BIOMES[context.nextRandom(MEDIUM_BIOMES.size)]
            3 -> if (shifted > 0) 32 else COLD_BIOMES[context.nextRandom(COLD_BIOMES.size)]
            4 -> ICE_BIOMES[context.nextRandom(ICE_BIOMES.size)]
            else -> 14
        }
    }

    companion object {

        private val LEGACY_WARM_BIOMES = intArrayOf(2, 4, 3, 6, 1, 5)
        private val WARM_BIOMES = intArrayOf(2, 2, 2, 35, 35, 1)
        private val MEDIUM_BIOMES = intArrayOf(4, 29, 3, 1, 27, 6)
        private val COLD_BIOMES = intArrayOf(4, 3, 5, 1)
        private val ICE_BIOMES = intArrayOf(12, 12, 12, 30)

        // Constants
        private val NORMAL = BiomeInitLayer(WARM_BIOMES)
        private val LEGACY = BiomeInitLayer(LEGACY_WARM_BIOMES)

        fun of(isLegacy: Boolean) = if (isLegacy) LEGACY else NORMAL
    }
}
