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
package org.kryptonmc.krypton.util.datafix.fixes.name

import com.mojang.datafixers.schemas.Schema

class RenameEntitiesFix(outputSchema: Schema, changesType: Boolean) : RenameEntityFix("RenameEntitiesFix", outputSchema, changesType) {

    override fun rename(name: String) = if (name.startsWith(BRED_PREFIX)) {
        val newName = "minecraft:${name.substring(BRED_PREFIX.length)}"
        REMAPPED_IDS.getOrDefault(newName, newName)
    } else {
        REMAPPED_IDS.getOrDefault(name, name)
    }

    companion object {

        private const val BRED_PREFIX = "minecraft:bred_"
        val REMAPPED_IDS = mapOf(
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
        val REMAPPED_BLOCKS = mapOf(
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
        val REMAPPED_ITEMS = REMAPPED_BLOCKS + mapOf(
            "minecraft:clownfish" to "minecraft:tropical_fish",
            "minecraft:chorus_fruit_popped" to "minecraft:popped_chorus_fruit",
            "minecraft:evocation_illager_spawn_egg" to "minecraft:evoker_spawn_egg",
            "minecraft:vindication_illager_spawn_egg" to "minecraft:vindicator_spawn_egg"
        )
    }
}
