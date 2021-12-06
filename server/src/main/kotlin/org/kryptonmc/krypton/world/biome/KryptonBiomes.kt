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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.registry.InternalRegistries

object KryptonBiomes {

    @JvmField val THE_VOID = register(BiomeKeys.THE_VOID, OverworldBiomes::theVoid)
    @JvmField val PLAINS = register(BiomeKeys.PLAINS, OverworldBiomes::plains)

    init {
        register(BiomeKeys.SUNFLOWER_PLAINS, OverworldBiomes::sunflowerPlains)
        register(BiomeKeys.SNOWY_PLAINS, OverworldBiomes::snowyPlains)
        register(BiomeKeys.ICE_SPIKES, OverworldBiomes::iceSpikes)
        register(BiomeKeys.DESERT, OverworldBiomes::desert)
        register(BiomeKeys.SWAMP, OverworldBiomes::swamp)
        register(BiomeKeys.FOREST, OverworldBiomes::forest)
        register(BiomeKeys.FLOWER_FOREST, OverworldBiomes::flowerForest)
        register(BiomeKeys.BIRCH_FOREST, OverworldBiomes::birchForest)
        register(BiomeKeys.DARK_FOREST, OverworldBiomes::darkForest)
        register(BiomeKeys.OLD_GROWTH_BIRCH_FOREST, OverworldBiomes::oldGrowthBirchForest)
        register(BiomeKeys.OLD_GROWTH_PINE_TAIGA, OverworldBiomes::oldGrowthPineTaiga)
        register(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA, OverworldBiomes::oldGrowthSpruceTaiga)
        register(BiomeKeys.TAIGA, OverworldBiomes::taiga)
        register(BiomeKeys.SNOWY_TAIGA, OverworldBiomes::snowyTaiga)
        register(BiomeKeys.SAVANNA, OverworldBiomes::savanna)
        register(BiomeKeys.SAVANNA_PLATEAU, OverworldBiomes::savannaPlateau)
        register(BiomeKeys.WINDSWEPT_HILLS, OverworldBiomes::windsweptHills)
        register(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS, OverworldBiomes::windsweptHills)
        register(BiomeKeys.WINDSWEPT_FOREST, OverworldBiomes::windsweptForest)
        register(BiomeKeys.WINDSWEPT_SAVANNA, OverworldBiomes::windsweptSavanna)
        register(BiomeKeys.JUNGLE, OverworldBiomes::jungle)
        register(BiomeKeys.SPARSE_JUNGLE, OverworldBiomes::sparseJungle)
        register(BiomeKeys.BAMBOO_JUNGLE, OverworldBiomes::bambooJungle)
        register(BiomeKeys.BADLANDS, OverworldBiomes::badlands)
        register(BiomeKeys.ERODED_BADLANDS, OverworldBiomes::badlands)
        register(BiomeKeys.WOODED_BADLANDS, OverworldBiomes::woodedBadlands)
        register(BiomeKeys.MEADOW, OverworldBiomes::meadow)
        register(BiomeKeys.GROVE, OverworldBiomes::grove)
        register(BiomeKeys.SNOWY_SLOPES, OverworldBiomes::snowySlopes)
        register(BiomeKeys.FROZEN_PEAKS, OverworldBiomes::frozenPeaks)
        register(BiomeKeys.JAGGED_PEAKS, OverworldBiomes::jaggedPeaks)
        register(BiomeKeys.STONY_PEAKS, OverworldBiomes::stonyPeaks)
        register(BiomeKeys.RIVER, OverworldBiomes::river)
        register(BiomeKeys.FROZEN_RIVER, OverworldBiomes::frozenRiver)
        register(BiomeKeys.BEACH, OverworldBiomes::beach)
        register(BiomeKeys.SNOWY_BEACH, OverworldBiomes::snowyBeach)
        register(BiomeKeys.STONY_SHORE, OverworldBiomes::stonyShore)
        register(BiomeKeys.WARM_OCEAN, OverworldBiomes::warmOcean)
        register(BiomeKeys.LUKEWARM_OCEAN, OverworldBiomes::lukewarmOcean)
        register(BiomeKeys.DEEP_LUKEWARM_OCEAN, OverworldBiomes::deepLukewarmOcean)
        register(BiomeKeys.OCEAN, OverworldBiomes::ocean)
        register(BiomeKeys.DEEP_OCEAN, OverworldBiomes::deepOcean)
        register(BiomeKeys.COLD_OCEAN, OverworldBiomes::coldOcean)
        register(BiomeKeys.DEEP_COLD_OCEAN, OverworldBiomes::deepColdOcean)
        register(BiomeKeys.FROZEN_OCEAN, OverworldBiomes::frozenOcean)
        register(BiomeKeys.DEEP_FROZEN_OCEAN, OverworldBiomes::deepFrozenOcean)
        register(BiomeKeys.MUSHROOM_FIELDS, OverworldBiomes::mushroomFields)
        register(BiomeKeys.DRIPSTONE_CAVES, OverworldBiomes::dripstoneCaves)
        register(BiomeKeys.LUSH_CAVES, OverworldBiomes::lushCaves)
        register(BiomeKeys.NETHER_WASTES, NetherBiomes::netherWastes)
        register(BiomeKeys.WARPED_FOREST, NetherBiomes::warpedForest)
        register(BiomeKeys.CRIMSON_FOREST, NetherBiomes::crimsonForest)
        register(BiomeKeys.SOUL_SAND_VALLEY, NetherBiomes::soulSandValley)
        register(BiomeKeys.BASALT_DELTAS, NetherBiomes::basaltDeltas)
        register(BiomeKeys.THE_END, EndBiomes::theEnd)
        register(BiomeKeys.END_HIGHLANDS, EndBiomes::endHighlands)
        register(BiomeKeys.END_MIDLANDS, EndBiomes::endMidlands)
        register(BiomeKeys.SMALL_END_ISLANDS, EndBiomes::smallEndIslands)
        register(BiomeKeys.END_BARRENS, EndBiomes::endBarrens)
    }

    @JvmStatic
    private fun register(key: ResourceKey<Biome>, provider: (Key) -> KryptonBiome): KryptonBiome = register(key, provider(key.location))

    @JvmStatic
    private fun register(key: ResourceKey<Biome>, biome: KryptonBiome): KryptonBiome = InternalRegistries.BIOME.register(key, biome)
}
