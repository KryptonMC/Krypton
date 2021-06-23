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
package org.kryptonmc.krypton.util.datafix.fixes.item

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.kryptonmc.krypton.util.datafix.References

class EnchantmentNamesFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val itemType = inputSchema.getType(References.ITEM_STACK)
        val tagFinder = itemType.findField("tag")
        return fixTypeEverywhereTyped("EnchantmentNamesFix", itemType) { typed -> typed.updateTyped(tagFinder) { tag -> tag.update(remainderFinder()) { it.fixTag() } } }
    }

    private fun Dynamic<*>.fixTag(): Dynamic<*> {
        var temp = this
        val enchants = get("ench").asStreamOpt()
            .map { stream -> stream.map { it.set("id", it.createString(MAP.getOrDefault(it["id"].asInt(0), "null"))) } }
            .map(::createList)
            .result()
        if (enchants.isPresent) temp = temp.remove("ench").set("Enchantments", enchants.get())
        return temp.update("StoredEnchantments") { dynamic ->
            DataFixUtils.orElse(dynamic.asStreamOpt()
                .map { stream -> stream.map { it.set("id", it.createString(MAP.getOrDefault(it["id"].asInt(0), "null"))) } }
                .map(::createList)
                .result(), dynamic)
        }
    }

    companion object {

        private val MAP = Int2ObjectOpenHashMap<String>().apply {
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
    }
}
