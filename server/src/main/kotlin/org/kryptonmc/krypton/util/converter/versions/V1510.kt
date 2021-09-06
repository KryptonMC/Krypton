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
package org.kryptonmc.krypton.util.converter.versions

import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converters.RenameBlocksConverter
import org.kryptonmc.krypton.util.converters.RenameEntitiesConverter
import org.kryptonmc.krypton.util.converters.RenameItemsConverter
import org.kryptonmc.krypton.util.converters.RenameRecipesConverter
import org.kryptonmc.krypton.util.converters.RenameStatsConverter

object V1510 {

    private const val VERSION = MCVersions.V1_13_PRE4 + 6
    private val RENAMED_ENTITY_IDS = mapOf(
        "minecraft:commandblock_minecart" to "minecraft:command_block_minecart",
        "minecraft:ender_crystal" to "minecraft:end_crystal",
        "minecraft:snowman" to "minecraft:snow_golem",
        "minecraft:evocation_illager" to "minecraft:evoker",
        "minecraft:evocation_fangs" to "minecraft:evoker_fangs",
        "minecraft:illusion_illager" to "minecraft:illusioner",
        "minecraft:vindication_illager" to "minecraft:vindicator",
        "minecraft:villager_golem" to "minecraft:iron_golem",
        "minecraft:xp_orb" to "minecraft:experience_orb",
        "minecraft:xp_bottle" to "minecraft:experience_bottle",
        "minecraft:eye_of_ender_signal" to "minecraft:eye_of_ender",
        "minecraft:fireworks_rocket" to "minecraft:firework_rocket"
    )
    private val RENAMED_BLOCKS = mapOf(
        "minecraft:portal" to "minecraft:nether_portal",
        "minecraft:oak_bark" to "minecraft:oak_wood",
        "minecraft:spruce_bark" to "minecraft:spruce_wood",
        "minecraft:birch_bark" to "minecraft:birch_wood",
        "minecraft:jungle_bark" to "minecraft:jungle_wood",
        "minecraft:acacia_bark" to "minecraft:acacia_wood",
        "minecraft:dark_oak_bark" to "minecraft:dark_oak_wood",
        "minecraft:stripped_oak_bark" to "minecraft:stripped_oak_wood",
        "minecraft:stripped_spruce_bark" to "minecraft:stripped_spruce_wood",
        "minecraft:stripped_birch_bark" to "minecraft:stripped_birch_wood",
        "minecraft:stripped_jungle_bark" to "minecraft:stripped_jungle_wood",
        "minecraft:stripped_acacia_bark" to "minecraft:stripped_acacia_wood",
        "minecraft:stripped_dark_oak_bark" to "minecraft:stripped_dark_oak_wood",
        "minecraft:mob_spawner" to "minecraft:spawner"
    )
    private val RENAMED_ITEMS = RENAMED_BLOCKS + mapOf(
        "minecraft:clownfish" to "minecraft:tropical_fish",
        "minecraft:chorus_fruit_popped" to "minecraft:popped_chorus_fruit",
        "minecraft:evocation_illager_spawn_egg" to "minecraft:evoker_spawn_egg",
        "minecraft:vindication_illager_spawn_egg" to "minecraft:vindicator_spawn_egg"
    )
    private val RECIPES_UPDATES = mapOf(
        "minecraft:acacia_bark" to "minecraft:acacia_wood",
        "minecraft:birch_bark" to "minecraft:birch_wood",
        "minecraft:dark_oak_bark" to "minecraft:dark_oak_wood",
        "minecraft:jungle_bark" to "minecraft:jungle_wood",
        "minecraft:oak_bark" to "minecraft:oak_wood",
        "minecraft:spruce_bark" to "minecraft:spruce_wood"
    )

    fun register() {
        RenameBlocksConverter.register(VERSION, RENAMED_BLOCKS::get)
        RenameItemsConverter.register(VERSION, RENAMED_ITEMS::get)
        RenameRecipesConverter.register(VERSION, RECIPES_UPDATES::get)
        RenameEntitiesConverter.register(VERSION) {
            var temp = it
            if (temp.startsWith("minecraft:bred_")) temp = "minecraft:${temp.substring("minecraft:bred_".length)}"
            RENAMED_ENTITY_IDS[temp]
        }
        RenameStatsConverter.register(VERSION, mapOf(
            "minecraft:swim_one_cm" to "minecraft:walk_on_water_one_cm",
            "minecraft:dive_one_cm" to "minecraft:walk_under_water_one_cm"
        )::get)

        MCTypeRegistry.ENTITY.copyWalkers(
            VERSION,
            "minecraft:commandblock_minecart",
            "minecraft:command_block_minecart"
        )
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:ender_crystal", "minecraft:end_crystal")
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:snowman", "minecraft:snow_golem")
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:evocation_illager", "minecraft:evoker")
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:evocation_fangs", "minecraft:evoker_fangs")
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:illusion_illager", "minecraft:illusioner")
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:vindication_illager", "minecraft:vindicator")
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:villager_golem", "minecraft:iron_golem")
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:xp_orb", "minecraft:experience_orb")
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:xp_bottle", "minecraft:experience_bottle")
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:eye_of_ender_signal", "minecraft:eye_of_ender")
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:fireworks_rocket", "minecraft:firework_rocket")
    }
}
