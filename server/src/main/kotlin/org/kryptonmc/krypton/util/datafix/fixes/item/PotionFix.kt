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

import com.mojang.datafixers.DSL.fieldFinder
import com.mojang.datafixers.DSL.named
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.util.Pair
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.schema.NamespacedSchema

class PotionFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val itemType = inputSchema.getType(References.ITEM_STACK)
        val idFinder = fieldFinder("id", named(References.ITEM_NAME.typeName(), NamespacedSchema.NAMESPACED_STRING))
        val tagFinder = itemType.findField("tag")
        return fixTypeEverywhereTyped("PotionFix", itemType) {
            val id = it.getOptional(idFinder)
            if (id.isEmpty || id.get().second != "minecraft:potion") return@fixTypeEverywhereTyped it
            var data = it[remainderFinder()]
            val tagTyped = it.getOptionalTyped(tagFinder)
            val damage = data["Damage"].asShort(0)
            if (tagTyped.isEmpty) return@fixTypeEverywhereTyped it
            var typed = it
            val tagData = tagTyped.get()[remainderFinder()]
            val potion = tagData["Potion"].asString().result()
            if (potion.isEmpty) {
                val newPotion = POTIONS[damage.toInt() and 127]
                val someTyped = tagTyped.get().set(remainderFinder(), tagData.set("Potion", tagData.createString(newPotion ?: DEFAULT)))
                typed = it.set(tagFinder, someTyped)
                if (damage.toInt() and SPLASH_ID == SPLASH_ID) typed = typed.set(idFinder, Pair.of(References.ITEM_NAME.typeName(), "minecraft:splash_potion"))
            }
            if (damage != 0.toShort()) data = data.set("Damage", data.createShort(0))
            typed.set(remainderFinder(), data)
        }
    }

    companion object {

        private const val SPLASH_ID = 16384
        private const val DEFAULT = "minecraft:water"
        private val POTIONS = arrayOf(
            "minecraft:water",
            "minecraft:regeneration",
            "minecraft:swiftness",
            "minecraft:fire_resistance",
            "minecraft:poison",
            "minecraft:healing",
            "minecraft:night_vision",
            null,
            "minecraft:weakness",
            "minecraft:strength",
            "minecraft:slowness",
            "minecraft:leaping",
            "minecraft:harming",
            "minecraft:water_breathing",
            "minecraft:invisibility",
            null,
            "minecraft:awkward",
            "minecraft:regeneration",
            "minecraft:swiftness",
            "minecraft:fire_resistance",
            "minecraft:poison",
            "minecraft:healing",
            "minecraft:night_vision",
            null,
            "minecraft:weakness",
            "minecraft:strength",
            "minecraft:slowness",
            "minecraft:leaping",
            "minecraft:harming",
            "minecraft:water_breathing",
            "minecraft:invisibility",
            null,
            "minecraft:thick",
            "minecraft:strong_regeneration",
            "minecraft:strong_swiftness",
            "minecraft:fire_resistance",
            "minecraft:strong_poison",
            "minecraft:strong_healing",
            "minecraft:night_vision",
            null,
            "minecraft:weakness",
            "minecraft:strong_strength",
            "minecraft:slowness",
            "minecraft:strong_leaping",
            "minecraft:strong_harming",
            "minecraft:water_breathing",
            "minecraft:invisibility",
            null,
            null,
            "minecraft:strong_regeneration",
            "minecraft:strong_swiftness",
            "minecraft:fire_resistance",
            "minecraft:strong_poison",
            "minecraft:strong_healing",
            "minecraft:night_vision",
            null,
            "minecraft:weakness",
            "minecraft:strong_strength",
            "minecraft:slowness",
            "minecraft:strong_leaping",
            "minecraft:strong_harming",
            "minecraft:water_breathing",
            "minecraft:invisibility",
            null,
            "minecraft:mundane",
            "minecraft:long_regeneration",
            "minecraft:long_swiftness",
            "minecraft:long_fire_resistance",
            "minecraft:long_poison",
            "minecraft:healing",
            "minecraft:long_night_vision",
            null,
            "minecraft:long_weakness",
            "minecraft:long_strength",
            "minecraft:long_slowness",
            "minecraft:long_leaping",
            "minecraft:harming",
            "minecraft:long_water_breathing",
            "minecraft:long_invisibility",
            null,
            "minecraft:awkward",
            "minecraft:long_regeneration",
            "minecraft:long_swiftness",
            "minecraft:long_fire_resistance",
            "minecraft:long_poison",
            "minecraft:healing",
            "minecraft:long_night_vision",
            null,
            "minecraft:long_weakness",
            "minecraft:long_strength",
            "minecraft:long_slowness",
            "minecraft:long_leaping",
            "minecraft:harming",
            "minecraft:long_water_breathing",
            "minecraft:long_invisibility",
            null,
            "minecraft:thick",
            "minecraft:regeneration",
            "minecraft:swiftness",
            "minecraft:long_fire_resistance",
            "minecraft:poison",
            "minecraft:strong_healing",
            "minecraft:long_night_vision",
            null,
            "minecraft:long_weakness",
            "minecraft:strength",
            "minecraft:long_slowness",
            "minecraft:leaping",
            "minecraft:strong_harming",
            "minecraft:long_water_breathing",
            "minecraft:long_invisibility",
            null,
            null,
            "minecraft:regeneration",
            "minecraft:swiftness",
            "minecraft:long_fire_resistance",
            "minecraft:poison",
            "minecraft:strong_healing",
            "minecraft:long_night_vision",
            null,
            "minecraft:long_weakness",
            "minecraft:strength",
            "minecraft:long_slowness",
            "minecraft:leaping",
            "minecraft:strong_harming",
            "minecraft:long_water_breathing",
            "minecraft:long_invisibility",
            null
        )
    }
}
