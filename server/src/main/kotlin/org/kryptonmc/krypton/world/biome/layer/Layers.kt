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

import it.unimi.dsi.fastutil.ints.Int2IntMap
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import org.kryptonmc.krypton.world.biome.BiomeConstants
import org.kryptonmc.krypton.world.biome.area.Area
import org.kryptonmc.krypton.world.biome.area.AreaFactory
import org.kryptonmc.krypton.world.biome.context.BigContext
import org.kryptonmc.krypton.world.biome.context.LazyAreaContext
import org.kryptonmc.krypton.world.biome.layer.traits.AreaTransformer1

object Layers {

    val CATEGORIES = Int2IntOpenHashMap().apply {
        put(Category.BEACH, BiomeConstants.BEACH)
        put(Category.BEACH, BiomeConstants.SNOWY_BEACH)
        put(Category.DESERT, BiomeConstants.DESERT)
        put(Category.DESERT, BiomeConstants.DESERT_HILLS)
        put(Category.DESERT, BiomeConstants.DESERT_LAKES)
        put(Category.EXTREME_HILLS, BiomeConstants.GRAVELLY_MOUNTAINS)
        put(Category.EXTREME_HILLS, BiomeConstants.MODIFIED_GRAVELLY_MOUNTAINS)
        put(Category.EXTREME_HILLS, BiomeConstants.MOUNTAIN_EDGE)
        put(Category.EXTREME_HILLS, BiomeConstants.MOUNTAINS)
        put(Category.EXTREME_HILLS, BiomeConstants.WOODED_MOUNTAINS)
        put(Category.FOREST, BiomeConstants.BIRCH_FOREST)
        put(Category.FOREST, BiomeConstants.BIRCH_FOREST_HILLS)
        put(Category.FOREST, BiomeConstants.DARK_FOREST)
        put(Category.FOREST, BiomeConstants.DARK_FOREST_HILLS)
        put(Category.FOREST, BiomeConstants.FLOWER_FOREST)
        put(Category.FOREST, BiomeConstants.FOREST)
        put(Category.FOREST, BiomeConstants.TALL_BIRCH_FOREST)
        put(Category.FOREST, BiomeConstants.TALL_BIRCH_HILLS)
        put(Category.FOREST, BiomeConstants.WOODED_HILLS)
        put(Category.ICY, BiomeConstants.ICE_SPIKES)
        put(Category.ICY, BiomeConstants.SNOWY_MOUNTAINS)
        put(Category.ICY, BiomeConstants.SNOWY_TUNDRA)
        put(Category.JUNGLE, BiomeConstants.BAMBOO_JUNGLE)
        put(Category.JUNGLE, BiomeConstants.BAMBOO_JUNGLE_HILLS)
        put(Category.JUNGLE, BiomeConstants.JUNGLE)
        put(Category.JUNGLE, BiomeConstants.JUNGLE_EDGE)
        put(Category.JUNGLE, BiomeConstants.JUNGLE_HILLS)
        put(Category.JUNGLE, BiomeConstants.MODIFIED_JUNGLE)
        put(Category.JUNGLE, BiomeConstants.MODIFIED_JUNGLE_EDGE)
        put(Category.MESA, BiomeConstants.BADLANDS)
        put(Category.MESA, BiomeConstants.ERODED_BADLANDS)
        put(Category.MESA, BiomeConstants.MODIFIED_BADLANDS_PLATEAU)
        put(Category.MESA, BiomeConstants.MODIFIED_WOODED_BADLANDS_PLATEAU)
        put(Category.BADLANDS_PLATEAU, BiomeConstants.BADLANDS_PLATEAU)
        put(Category.BADLANDS_PLATEAU, BiomeConstants.WOODED_BADLANDS_PLATEAU)
        put(Category.MUSHROOM, BiomeConstants.MUSHROOM_FIELDS)
        put(Category.MUSHROOM, BiomeConstants.MUSHROOM_FIELD_SHORE)
        put(Category.NONE, BiomeConstants.STONE_SHORE)
        put(Category.OCEAN, BiomeConstants.COLD_OCEAN)
        put(Category.OCEAN, BiomeConstants.DEEP_COLD_OCEAN)
        put(Category.OCEAN, BiomeConstants.DEEP_FROZEN_OCEAN)
        put(Category.OCEAN, BiomeConstants.DEEP_LUKEWARM_OCEAN)
        put(Category.OCEAN, BiomeConstants.DEEP_OCEAN)
        put(Category.OCEAN, BiomeConstants.DEEP_WARM_OCEAN)
        put(Category.OCEAN, BiomeConstants.FROZEN_OCEAN)
        put(Category.OCEAN, BiomeConstants.LUKEWARM_OCEAN)
        put(Category.OCEAN, BiomeConstants.OCEAN)
        put(Category.OCEAN, BiomeConstants.WARM_OCEAN)
        put(Category.PLAINS, BiomeConstants.PLAINS)
        put(Category.PLAINS, BiomeConstants.SUNFLOWER_PLAINS)
        put(Category.RIVER, BiomeConstants.FROZEN_RIVER)
        put(Category.RIVER, BiomeConstants.RIVER)
        put(Category.SAVANNA, BiomeConstants.SAVANNA)
        put(Category.SAVANNA, BiomeConstants.SAVANNA_PLATEAU)
        put(Category.SAVANNA, BiomeConstants.SHATTERED_SAVANNA)
        put(Category.SAVANNA, BiomeConstants.SHATTERED_SAVANNA_PLATEAU)
        put(Category.SWAMP, BiomeConstants.SWAMP)
        put(Category.SWAMP, BiomeConstants.SWAMP_HILLS)
        put(Category.TAIGA, BiomeConstants.GIANT_SPRUCE_TAIGA)
        put(Category.TAIGA, BiomeConstants.GIANT_SPRUCE_TAIGA_HILLS)
        put(Category.TAIGA, BiomeConstants.GIANT_TREE_TAIGA)
        put(Category.TAIGA, BiomeConstants.GIANT_TREE_TAIGA_HILLS)
        put(Category.TAIGA, BiomeConstants.SNOWY_TAIGA)
        put(Category.TAIGA, BiomeConstants.SNOWY_TAIGA_HILLS)
        put(Category.TAIGA, BiomeConstants.SNOWY_TAIGA_MOUNTAINS)
        put(Category.TAIGA, BiomeConstants.TAIGA)
        put(Category.TAIGA, BiomeConstants.TAIGA_HILLS)
        put(Category.TAIGA, BiomeConstants.TAIGA_MOUNTAINS)
    }

    fun default(seed: Long, isLegacy: Boolean, value: Int, iterations: Int) =
        Layer(default(isLegacy, value, iterations) { LazyAreaContext(25, seed, it) })

    private fun <A : Area, C : BigContext<A>> default(isLegacy: Boolean, zoomRuns: Int, iterations: Int, getter: (Long) -> C): AreaFactory<A> {
        var factory = IslandLayer.run(getter(1L))
        factory = ZoomLayer.FUZZY.run(getter(2000L), factory)
        factory = AddIslandLayer.run(getter(1L), factory)
        factory = ZoomLayer.NORMAL.run(getter(2001L), factory)
        factory = AddIslandLayer.run(getter(2L), factory)
        factory = AddIslandLayer.run(getter(50L), factory)
        factory = AddIslandLayer.run(getter(70L), factory)
        factory = RemoveTooMuchOceanLayer.run(getter(2L), factory)
        var factory1 = OceanLayer.run(getter(2L))
        factory1 = zoom(2001L, ZoomLayer.NORMAL, factory1, 6, getter)
        factory = AddSnowLayer.run(getter(2L), factory)
        factory = AddIslandLayer.run(getter(3L), factory)
        factory = AddEdgeLayer.CoolWarm.run(getter(2L), factory)
        factory = AddEdgeLayer.HeatIce.run(getter(2L), factory)
        factory = AddEdgeLayer.IntroduceSpecial.run(getter(3L), factory)
        factory = ZoomLayer.NORMAL.run(getter(2002L), factory)
        factory = ZoomLayer.NORMAL.run(getter(2003L), factory)
        factory = AddIslandLayer.run(getter(4L), factory)
        factory = AddMushroomIslandLayer.run(getter(5L), factory)
        factory = AddDeepOceanLayer.run(getter(4L), factory)
        factory = zoom(1000L, ZoomLayer.NORMAL, factory, 0, getter)
        var factory2 = zoom(1000L, ZoomLayer.NORMAL, factory, 0, getter)
        factory2 = RiverInitLayer.run(getter(100L), factory2)
        var factory3 = BiomeInitLayer(isLegacy).run(getter(200L), factory)
        factory3 = RareBiomeLargeLayer.run(getter(1001L), factory3)
        factory3 = zoom(1000L, ZoomLayer.NORMAL, factory3, 2, getter)
        factory3 = BiomeEdgeLayer.run(getter(1000L), factory3)
        val factory4 = zoom(1000L, ZoomLayer.NORMAL, factory2, 2, getter)
        factory3 = RegionHillsLayer.run(getter(1000L), factory3, factory4)
        factory2 = zoom(1000L, ZoomLayer.NORMAL, factory2, 2, getter)
        factory2 = zoom(1000L, ZoomLayer.NORMAL, factory2, iterations, getter)
        factory2 = RiverLayer.run(getter(1L), factory2)
        factory2 = SmoothLayer.run(getter(1000L), factory2)
        factory3 = RareBiomeSpotLayer.run(getter(1001L), factory3)
        for (i in 0 until zoomRuns) {
            factory3 = ZoomLayer.NORMAL.run(getter((1000 + i).toLong()), factory3)
            if (i == 0) factory3 = AddIslandLayer.run(getter(3L), factory3)
            if (i == 1 || zoomRuns == 1) factory3 = ShoreLayer.run(getter(1000L), factory3)
        }
        factory3 = SmoothLayer.run(getter(1000L), factory3)
        factory3 = RiverMixerLayer.run(getter(100L), factory3, factory2)
        return OceanMixerLayer.run(getter(100L), factory3, factory1)
    }

    private fun <A : Area, C : BigContext<A>> zoom(seed: Long, transformer: AreaTransformer1, factory: AreaFactory<A>, iterations: Int, getter: (Long) -> C): AreaFactory<A> {
        var temp = factory
        for (i in 0 until iterations) temp = transformer.run(getter(seed + i.toLong()), temp)
        return temp
    }

    enum class Category {

        NONE,
        TAIGA,
        EXTREME_HILLS,
        JUNGLE,
        MESA,
        BADLANDS_PLATEAU,
        PLAINS,
        SAVANNA,
        ICY,
        BEACH,
        FOREST,
        OCEAN,
        DESERT,
        RIVER,
        SWAMP,
        MUSHROOM
    }
}

private fun Int2IntMap.put(category: Layers.Category, biome: Int) = put(biome, category.ordinal)
