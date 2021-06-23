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

class ShulkerBoxColorFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val itemType = inputSchema.getType(References.ITEM_STACK)
        val idFinder = fieldFinder("id", named(References.ITEM_NAME.typeName(), NamespacedSchema.NAMESPACED_STRING))
        val tagFinder = itemType.findField("tag")
        val entityTagFinder = tagFinder.type().findField("BlockEntityTag")
        return fixTypeEverywhereTyped("ItemShulkerBoxColorFix", itemType) { typed ->
            val id = typed.getOptional(idFinder)
            if (id.isEmpty || id.get().second != "minecraft:shulker_box") return@fixTypeEverywhereTyped typed
            val tagTyped = typed.getOptionalTyped(tagFinder).takeIf { it.isPresent }?.get() ?: return@fixTypeEverywhereTyped typed
            val entityTagTyped = tagTyped.getOptionalTyped(entityTagFinder).takeIf { it.isPresent }?.get() ?: return@fixTypeEverywhereTyped typed
            val data = entityTagTyped[remainderFinder()]
            val color = data["Color"].asInt(0)
            data.remove("Color")
            typed.set(tagFinder, tagTyped.set(entityTagFinder, entityTagTyped.set(remainderFinder(), data))).set(idFinder, Pair.of(References.ITEM_NAME.typeName(), NAMES_BY_COLOR[color % NAMES_BY_COLOR.size]))
        }
    }

    companion object {

        val NAMES_BY_COLOR = arrayOf(
            "minecraft:white_shulker_box",
            "minecraft:orange_shulker_box",
            "minecraft:magenta_shulker_box",
            "minecraft:light_blue_shulker_box",
            "minecraft:yellow_shulker_box",
            "minecraft:lime_shulker_box",
            "minecraft:pink_shulker_box",
            "minecraft:gray_shulker_box",
            "minecraft:silver_shulker_box",
            "minecraft:cyan_shulker_box",
            "minecraft:purple_shulker_box",
            "minecraft:blue_shulker_box",
            "minecraft:brown_shulker_box",
            "minecraft:green_shulker_box",
            "minecraft:red_shulker_box",
            "minecraft:black_shulker_box"
        )
    }
}
