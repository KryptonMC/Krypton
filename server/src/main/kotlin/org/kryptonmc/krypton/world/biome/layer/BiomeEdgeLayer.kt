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

import org.kryptonmc.krypton.world.biome.BiomeConstants
import org.kryptonmc.krypton.world.biome.context.Context
import org.kryptonmc.krypton.world.biome.layer.traits.CastleTransformer

object BiomeEdgeLayer : CastleTransformer {

    override fun invoke(context: Context, n: Int, e: Int, s: Int, w: Int, center: Int): Int {
        val array = IntArray(1)
        if (checkEdge(array, center) || checkEdgeStrict(array, n, e, s, w, center, 38, 37) || checkEdgeStrict(array, n, e, s, w, center, 39, 37) || checkEdgeStrict(array, n, e, s, w, center, 32, 5)) return array[0]
        if (center != 2 || n != BiomeConstants.SNOWY_TAIGA && e != BiomeConstants.SNOWY_TAIGA && s != BiomeConstants.SNOWY_TAIGA && w != BiomeConstants.SNOWY_TAIGA) {
            if (center == 6) {
                if (
                    n == BiomeConstants.DESERT || e == BiomeConstants.DESERT || s == BiomeConstants.DESERT || w == BiomeConstants.DESERT ||
                    n == BiomeConstants.SNOWY_TAIGA || e == BiomeConstants.SNOWY_TAIGA || s == BiomeConstants.SNOWY_TAIGA || w == BiomeConstants.SNOWY_TAIGA ||
                    n == BiomeConstants.SNOWY_TUNDRA || e == BiomeConstants.SNOWY_TUNDRA || s == BiomeConstants.SNOWY_TUNDRA || w == BiomeConstants.SNOWY_TUNDRA
                ) return BiomeConstants.PLAINS
                if (
                    n == BiomeConstants.JUNGLE || e == BiomeConstants.JUNGLE || s == BiomeConstants.JUNGLE || w == BiomeConstants.JUNGLE ||
                    n == BiomeConstants.BAMBOO_JUNGLE || e == BiomeConstants.BAMBOO_JUNGLE || s == BiomeConstants.BAMBOO_JUNGLE || w == BiomeConstants.BAMBOO_JUNGLE
                ) return BiomeConstants.JUNGLE_EDGE
            }
            return center
        }
        return 34
    }

    private fun checkEdge(array: IntArray, value: Int) = if (value.isSame(3)) {
        array[0] = value
        true
    } else false

    private fun checkEdgeStrict(array: IntArray, n: Int, e: Int, s: Int, w: Int, center: Int, value: Int, default: Int): Boolean {
        if (center != value) return false
        array[0] = if (n.isSame(value) && e.isSame(value) && s.isSame(value) && w.isSame(value)) center else default
        return true
    }
}
