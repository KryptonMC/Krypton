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

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.biome.BiomeConstants
import org.kryptonmc.krypton.world.biome.area.Area
import org.kryptonmc.krypton.world.biome.context.Context
import org.kryptonmc.krypton.world.biome.layer.traits.AreaTransformer2
import org.kryptonmc.krypton.world.biome.layer.traits.DimensionOffset1Transformer

object RegionHillsLayer : AreaTransformer2, DimensionOffset1Transformer {

    private val LOGGER = logger<RegionHillsLayer>()
    private val MUTATIONS = Int2IntOpenHashMap().apply {
        put(BiomeConstants.PLAINS, BiomeConstants.SUNFLOWER_PLAINS)
        put(BiomeConstants.DESERT, BiomeConstants.DESERT_LAKES)
        put(BiomeConstants.MOUNTAINS, BiomeConstants.GRAVELLY_MOUNTAINS)
        put(BiomeConstants.FOREST, BiomeConstants.FLOWER_FOREST)
        put(BiomeConstants.TAIGA, BiomeConstants.TAIGA_MOUNTAINS)
        put(BiomeConstants.SWAMP, BiomeConstants.SWAMP_HILLS)
        put(BiomeConstants.SNOWY_TUNDRA, BiomeConstants.ICE_SPIKES)
        put(BiomeConstants.JUNGLE, BiomeConstants.MODIFIED_JUNGLE)
        put(BiomeConstants.JUNGLE_EDGE, BiomeConstants.MODIFIED_JUNGLE_EDGE)
        put(BiomeConstants.BIRCH_FOREST, BiomeConstants.TALL_BIRCH_FOREST)
        put(BiomeConstants.BIRCH_FOREST_HILLS, BiomeConstants.TALL_BIRCH_HILLS)
        put(BiomeConstants.DARK_FOREST, BiomeConstants.DARK_FOREST_HILLS)
        put(BiomeConstants.SNOWY_TAIGA, BiomeConstants.SNOWY_TAIGA_MOUNTAINS)
        put(BiomeConstants.GIANT_TREE_TAIGA, BiomeConstants.GIANT_SPRUCE_TAIGA)
        put(BiomeConstants.GIANT_TREE_TAIGA_HILLS, BiomeConstants.GIANT_SPRUCE_TAIGA_HILLS)
        put(BiomeConstants.WOODED_MOUNTAINS, BiomeConstants.MODIFIED_GRAVELLY_MOUNTAINS)
        put(BiomeConstants.SAVANNA, BiomeConstants.SHATTERED_SAVANNA)
        put(BiomeConstants.SAVANNA_PLATEAU, BiomeConstants.SHATTERED_SAVANNA_PLATEAU)
        put(BiomeConstants.BADLANDS, BiomeConstants.ERODED_BADLANDS)
        put(BiomeConstants.WOODED_BADLANDS_PLATEAU, BiomeConstants.MODIFIED_WOODED_BADLANDS_PLATEAU)
        put(BiomeConstants.BADLANDS_PLATEAU, BiomeConstants.MODIFIED_BADLANDS_PLATEAU)
    }

    override fun invoke(context: Context, firstParent: Area, secondParent: Area, x: Int, z: Int): Int {
        val firstValue = firstParent[parentX(x + 1), parentZ(z + 1)]
        val secondValue = secondParent[parentX(x + 1), parentZ(z + 1)]
        val value = (secondValue - 2) % 29
        if (!firstValue.isShallowOcean() && secondValue >= 2 && value == 1) return MUTATIONS.getOrDefault(firstValue, firstValue)
        if (context.nextRandom(3) == 0 || value == 0) {
            var mixed = when (firstValue) {
                BiomeConstants.DESERT -> BiomeConstants.DESERT_HILLS
                BiomeConstants.FOREST -> BiomeConstants.WOODED_HILLS
                BiomeConstants.BIRCH_FOREST -> BiomeConstants.BIRCH_FOREST_HILLS
                BiomeConstants.DARK_FOREST -> BiomeConstants.PLAINS
                BiomeConstants.TAIGA -> BiomeConstants.TAIGA_HILLS
                BiomeConstants.GIANT_TREE_TAIGA -> BiomeConstants.GIANT_TREE_TAIGA_HILLS
                BiomeConstants.SNOWY_TAIGA -> BiomeConstants.SNOWY_TAIGA_HILLS
                BiomeConstants.PLAINS -> {
                    if (context.nextRandom(3) == 0) BiomeConstants.WOODED_HILLS else BiomeConstants.FOREST
                }
                BiomeConstants.SNOWY_TUNDRA -> BiomeConstants.SNOWY_MOUNTAINS
                BiomeConstants.JUNGLE -> BiomeConstants.JUNGLE_HILLS
                BiomeConstants.BAMBOO_JUNGLE -> BiomeConstants.BAMBOO_JUNGLE_HILLS
                BiomeConstants.OCEAN -> BiomeConstants.DEEP_OCEAN
                BiomeConstants.LUKEWARM_OCEAN -> BiomeConstants.DEEP_LUKEWARM_OCEAN
                BiomeConstants.COLD_OCEAN -> BiomeConstants.DEEP_COLD_OCEAN
                BiomeConstants.FROZEN_OCEAN -> BiomeConstants.DEEP_FROZEN_OCEAN
                BiomeConstants.MOUNTAINS -> BiomeConstants.WOODED_MOUNTAINS
                BiomeConstants.SAVANNA -> BiomeConstants.SAVANNA_PLATEAU
                else -> 0
            }
            if (firstValue.isSame(BiomeConstants.WOODED_BADLANDS_PLATEAU)) mixed = BiomeConstants.BADLANDS
            if (
                (firstValue == BiomeConstants.DEEP_OCEAN || firstValue == BiomeConstants.DEEP_LUKEWARM_OCEAN ||
                        firstValue == BiomeConstants.DEEP_COLD_OCEAN || firstValue == BiomeConstants.DEEP_FROZEN_OCEAN) &&
                context.nextRandom(3) == 0
            ) {
                mixed = if (context.nextRandom(2) == 0) 1 else 4
            }
            if (value == 0 && mixed != firstValue) mixed = MUTATIONS.getOrDefault(mixed, firstValue)
            if (mixed != firstValue) {
                var i = 0
                if (firstParent[parentX(x + 1), parentZ(z)].isSame(firstValue)) i++
                if (firstParent[parentX(x + 2), parentZ(z + 1)].isSame(firstValue)) i++
                if (firstParent[parentX(x), parentZ(z + 1)].isSame(firstValue)) i++
                if (firstParent[parentX(x + 1), parentZ(z + 2)].isSame(firstValue)) i++
                if (i >= 3) return mixed
            }
        }
        return firstValue
    }
}
