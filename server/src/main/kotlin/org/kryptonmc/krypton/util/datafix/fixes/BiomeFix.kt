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
package org.kryptonmc.krypton.util.datafix.fixes

import com.mojang.datafixers.schemas.Schema
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameBiomesFix

class BiomeFix(outputSchema: Schema, changesType: Boolean) : RenameBiomesFix(outputSchema, changesType, "Biomes fix", BIOMES) {

    companion object {

        val BIOMES = mapOf(
            "minecraft:extreme_hills" to "minecraft:mountains",
            "minecraft:swampland" to "minecraft:swamp",
            "minecraft:hell" to "minecraft:nether_wastes",
            "minecraft:sky" to "minecraft:the_end",
            "minecraft:ice_flats" to "minecraft:snowy_tundra",
            "minecraft:ice_mountains" to "minecraft:snowy_mountains",
            "minecraft:mushroom_island" to "minecraft:mushroom_fields",
            "minecraft:mushroom_island_shore" to "minecraft:mushroom_field_shore",
            "minecraft:beaches" to "minecraft:beach",
            "minecraft:forest_hills" to "minecraft:wooded_hills",
            "minecraft:smaller_extreme_hills" to "minecraft:mountain_edge",
            "minecraft:stone_beach" to "minecraft:stone_shore",
            "minecraft:cold_beach" to "minecraft:snowy_beach",
            "minecraft:roofed_forest" to "minecraft:dark_forest",
            "minecraft:taiga_cold" to "minecraft:snowy_taiga",
            "minecraft:taiga_cold_hills" to "minecraft:snowy_taiga_hills",
            "minecraft:redwood_taiga" to "minecraft:giant_tree_taiga",
            "minecraft:redwood_taiga_hills" to "minecraft:giant_tree_taiga_hills",
            "minecraft:extreme_hills_with_trees" to "minecraft:wooded_mountains",
            "minecraft:savanna_rock" to "minecraft:savanna_plateau",
            "minecraft:mesa" to "minecraft:badlands",
            "minecraft:mesa_rock" to "minecraft:wooded_badlands_plateau",
            "minecraft:mesa_clear_rock" to "minecraft:badlands_plateau",
            "minecraft:sky_island_low" to "minecraft:small_end_islands",
            "minecraft:sky_island_medium" to "minecraft:end_midlands",
            "minecraft:sky_island_high" to "minecraft:end_highlands",
            "minecraft:sky_island_barren" to "minecraft:end_barrens",
            "minecraft:void" to "minecraft:the_void",
            "minecraft:mutated_plains" to "minecraft:sunflower_plains",
            "minecraft:mutated_desert" to "minecraft:desert_lakes",
            "minecraft:mutated_extreme_hills" to "minecraft:gravelly_mountains",
            "minecraft:mutated_forest" to "minecraft:flower_forest",
            "minecraft:mutated_taiga" to "minecraft:taiga_mountains",
            "minecraft:mutated_swampland" to "minecraft:swamp_hills",
            "minecraft:mutated_ice_flats" to "minecraft:ice_spikes",
            "minecraft:mutated_jungle" to "minecraft:modified_jungle",
            "minecraft:mutated_jungle_edge" to "minecraft:modified_jungle_edge",
            "minecraft:mutated_birch_forest" to "minecraft:tall_birch_forest",
            "minecraft:mutated_birch_forest_hills" to "minecraft:tall_birch_hills",
            "minecraft:mutated_roofed_forest" to "minecraft:dark_forest_hills",
            "minecraft:mutated_taiga_cold" to "minecraft:snowy_taiga_mountains",
            "minecraft:mutated_redwood_taiga" to "minecraft:giant_spruce_taiga",
            "minecraft:mutated_redwood_taiga_hills" to "minecraft:giant_spruce_taiga_hills",
            "minecraft:mutated_extreme_hills_with_trees" to "minecraft:modified_gravelly_mountains",
            "minecraft:mutated_savanna" to "minecraft:shattered_savanna",
            "minecraft:mutated_savanna_rock" to "minecraft:shattered_savanna_plateau",
            "minecraft:mutated_mesa" to "minecraft:eroded_badlands",
            "minecraft:mutated_mesa_rock" to "minecraft:modified_wooded_badlands_plateau",
            "minecraft:mutated_mesa_clear_rock" to "minecraft:modified_badlands_plateau",
            "minecraft:warm_deep_ocean" to "minecraft:deep_warm_ocean",
            "minecraft:lukewarm_deep_ocean" to "minecraft:deep_lukewarm_ocean",
            "minecraft:cold_deep_ocean" to "minecraft:deep_cold_ocean",
            "minecraft:frozen_deep_ocean" to "minecraft:deep_frozen_ocean"
        )
    }
}
