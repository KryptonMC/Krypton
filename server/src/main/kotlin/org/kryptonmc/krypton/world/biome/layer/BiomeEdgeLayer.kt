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

    override fun invoke(context: Context, north: Int, east: Int, south: Int, west: Int, center: Int): Int {
        val array = IntArray(1)
        if (
            checkEdge(array, center) ||
            checkEdgeStrict(array, north, east, south, west, center, 38, 37) ||
            checkEdgeStrict(array, north, east, south, west, center, 39, 37) ||
            checkEdgeStrict(array, north, east, south, west, center, 32, 5)
        ) {
            return array[0]
        }
        if (
            center != 2 ||
            north != BiomeConstants.SNOWY_TAIGA &&
            east != BiomeConstants.SNOWY_TAIGA &&
            south != BiomeConstants.SNOWY_TAIGA &&
            west != BiomeConstants.SNOWY_TAIGA
        ) {
            if (center == 6) {
                if (
                    north == BiomeConstants.DESERT || east == BiomeConstants.DESERT ||
                    south == BiomeConstants.DESERT || west == BiomeConstants.DESERT ||
                    north == BiomeConstants.SNOWY_TAIGA || east == BiomeConstants.SNOWY_TAIGA ||
                    south == BiomeConstants.SNOWY_TAIGA || west == BiomeConstants.SNOWY_TAIGA ||
                    north == BiomeConstants.SNOWY_TUNDRA || east == BiomeConstants.SNOWY_TUNDRA ||
                    south == BiomeConstants.SNOWY_TUNDRA || west == BiomeConstants.SNOWY_TUNDRA
                ) {
                    return BiomeConstants.PLAINS
                }
                if (
                    north == BiomeConstants.JUNGLE || east == BiomeConstants.JUNGLE ||
                    south == BiomeConstants.JUNGLE || west == BiomeConstants.JUNGLE ||
                    north == BiomeConstants.BAMBOO_JUNGLE || east == BiomeConstants.BAMBOO_JUNGLE ||
                    south == BiomeConstants.BAMBOO_JUNGLE || west == BiomeConstants.BAMBOO_JUNGLE
                ) {
                    return BiomeConstants.JUNGLE_EDGE
                }
            }
            return center
        }
        return 34
    }

    private fun checkEdge(array: IntArray, value: Int): Boolean {
        if (value.isSame(3)) {
            array[0] = value
            return true
        }
        return false
    }

    private fun checkEdgeStrict(
        array: IntArray,
        north: Int,
        east: Int,
        south: Int,
        west: Int,
        center: Int,
        value: Int,
        default: Int
    ): Boolean {
        if (center != value) return false
        if (north.isSame(value) && east.isSame(value) && south.isSame(value) && west.isSame(value)) {
            array[0] = center
        } else {
            array[0] = default
        }
        return true
    }
}
