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

import ca.spottedleaf.dataconverter.types.ObjectType
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V1494 {

    private const val VERSION = MCVersions.V18W20C + 1
    private val ENCH_ID_TO_NAME = Int2ObjectOpenHashMap<String>().apply {
        put(0, "minecraft:protection")
        put(1, "minecraft:fire_protection")
        put(2, "minecraft:feather_falling")
        put(3, "minecraft:blast_protection")
        put(4, "minecraft:projectile_protection")
        put(5, "minecraft:respiration")
        put(6, "minecraft:aqua_affinity")
        put(7, "minecraft:thorns")
        put(8, "minecraft:depth_strider")
        put(9, "minecraft:frost_walker")
        put(10, "minecraft:binding_curse")
        put(16, "minecraft:sharpness")
        put(17, "minecraft:smite")
        put(18, "minecraft:bane_of_arthropods")
        put(19, "minecraft:knockback")
        put(20, "minecraft:fire_aspect")
        put(21, "minecraft:looting")
        put(22, "minecraft:sweeping")
        put(32, "minecraft:efficiency")
        put(33, "minecraft:silk_touch")
        put(34, "minecraft:unbreaking")
        put(35, "minecraft:fortune")
        put(48, "minecraft:power")
        put(49, "minecraft:punch")
        put(50, "minecraft:flame")
        put(51, "minecraft:infinity")
        put(61, "minecraft:luck_of_the_sea")
        put(62, "minecraft:lure")
        put(65, "minecraft:loyalty")
        put(66, "minecraft:impaling")
        put(67, "minecraft:riptide")
        put(68, "minecraft:channeling")
        put(70, "minecraft:mending")
        put(71, "minecraft:vanishing_curse")
    }

    fun register() = MCTypeRegistry.ITEM_STACK.addStructureConverter(VERSION) { data, _, _ ->
        val tag = data.getMap<String>("tag") ?: return@addStructureConverter null

        tag.getList("ench", ObjectType.MAP)?.let {
            tag.remove("ench")
            tag.setList("Enchantments", it)

            for (i in 0 until it.size()) {
                val enchant = it.getMap<String>(i)
                enchant.setString("id", ENCH_ID_TO_NAME.getOrDefault(enchant.getInt("id"), "null"))
            }
        }

        tag.getList("StoredEnchantments", ObjectType.MAP)?.let {
            for (i in 0 until it.size()) {
                val enchant = it.getMap<String>(i)
                enchant.setString("id", ENCH_ID_TO_NAME.getOrDefault(enchant.getInt("id"), "null"))
            }
        }
        null
    }
}
