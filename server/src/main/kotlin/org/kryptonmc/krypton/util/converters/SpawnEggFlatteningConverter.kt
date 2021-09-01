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
package org.kryptonmc.krypton.util.converters

import ca.spottedleaf.dataconverter.types.MapType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.StringDataConverter

object SpawnEggFlatteningConverter : StringDataConverter(MCVersions.V17W47A, 5) {

    private val ENTITY_ID_TO_NEW_EGG_ID = mapOf(
        "minecraft:bat" to "minecraft:bat_spawn_egg",
        "minecraft:blaze" to "minecraft:blaze_spawn_egg",
        "minecraft:cave_spider" to "minecraft:cave_spider_spawn_egg",
        "minecraft:chicken" to "minecraft:chicken_spawn_egg",
        "minecraft:cow" to "minecraft:cow_spawn_egg",
        "minecraft:creeper" to "minecraft:creeper_spawn_egg",
        "minecraft:donkey" to "minecraft:donkey_spawn_egg",
        "minecraft:elder_guardian" to "minecraft:elder_guardian_spawn_egg",
        "minecraft:enderman" to "minecraft:enderman_spawn_egg",
        "minecraft:endermite" to "minecraft:endermite_spawn_egg",
        "minecraft:evocation_illager" to "minecraft:evocation_illager_spawn_egg",
        "minecraft:ghast" to "minecraft:ghast_spawn_egg",
        "minecraft:guardian" to "minecraft:guardian_spawn_egg",
        "minecraft:horse" to "minecraft:horse_spawn_egg",
        "minecraft:husk" to "minecraft:husk_spawn_egg",
        "minecraft:llama" to "minecraft:llama_spawn_egg",
        "minecraft:magma_cube" to "minecraft:magma_cube_spawn_egg",
        "minecraft:mooshroom" to "minecraft:mooshroom_spawn_egg",
        "minecraft:mule" to "minecraft:mule_spawn_egg",
        "minecraft:ocelot" to "minecraft:ocelot_spawn_egg",
        "minecraft:pufferfish" to "minecraft:pufferfish_spawn_egg",
        "minecraft:parrot" to "minecraft:parrot_spawn_egg",
        "minecraft:pig" to "minecraft:pig_spawn_egg",
        "minecraft:polar_bear" to "minecraft:polar_bear_spawn_egg",
        "minecraft:rabbit" to "minecraft:rabbit_spawn_egg",
        "minecraft:sheep" to "minecraft:sheep_spawn_egg",
        "minecraft:shulker" to "minecraft:shulker_spawn_egg",
        "minecraft:silverfish" to "minecraft:silverfish_spawn_egg",
        "minecraft:skeleton" to "minecraft:skeleton_spawn_egg",
        "minecraft:skeleton_horse" to "minecraft:skeleton_horse_spawn_egg",
        "minecraft:slime" to "minecraft:slime_spawn_egg",
        "minecraft:spider" to "minecraft:spider_spawn_egg",
        "minecraft:squid" to "minecraft:squid_spawn_egg",
        "minecraft:stray" to "minecraft:stray_spawn_egg",
        "minecraft:turtle" to "minecraft:turtle_spawn_egg",
        "minecraft:vex" to "minecraft:vex_spawn_egg",
        "minecraft:villager" to "minecraft:villager_spawn_egg",
        "minecraft:vindication_illager" to "minecraft:vindication_illager_spawn_egg",
        "minecraft:witch" to "minecraft:witch_spawn_egg",
        "minecraft:wither_skeleton" to "minecraft:wither_skeleton_spawn_egg",
        "minecraft:wolf" to "minecraft:wolf_spawn_egg",
        "minecraft:zombie" to "minecraft:zombie_spawn_egg",
        "minecraft:zombie_horse" to "minecraft:zombie_horse_spawn_egg",
        "minecraft:zombie_pigman" to "minecraft:zombie_pigman_spawn_egg",
        "minecraft:zombie_villager" to "minecraft:zombie_villager_spawn_egg",
    )

    override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
        val tag = data.getMap<String>("tag") ?: return null
        val entityTag = tag.getMap<String>("EntityTag") ?: return null
        val id = entityTag.getString("id")
        if (id != null) data.setString("id", ENTITY_ID_TO_NEW_EGG_ID.getOrDefault(id, "minecraft:pig_spawn_egg"))
        return null
    }
}
