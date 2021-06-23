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
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.schema.NamespacedSchema
import java.util.stream.Stream

class BannerColorFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val itemType = inputSchema.getType(References.ITEM_STACK)
        val idFinder = fieldFinder("id", named(References.ITEM_NAME.typeName(), NamespacedSchema.NAMESPACED_STRING))
        val tagFinder = itemType.findField("tag")
        val entityTagFinder = tagFinder.type().findField("BlockEntityTag")
        return fixTypeEverywhereTyped("BannerItemColorFix", itemType) { typed ->
            val optionalId = typed.getOptional(idFinder)
            if (optionalId.isEmpty || optionalId.get().second != "minecraft:banner") return@fixTypeEverywhereTyped typed
            var data = typed[remainderFinder()]
            val optionalTagTyped = typed.getOptionalTyped(tagFinder)
            if (optionalTagTyped.isEmpty) return@fixTypeEverywhereTyped typed.set(remainderFinder(), data)
            val tagTyped = optionalTagTyped.get()
            val optionalEntityTagTyped = typed.getOptionalTyped(entityTagFinder)
            if (optionalEntityTagTyped.isEmpty) return@fixTypeEverywhereTyped typed.set(remainderFinder(), data)
            val entityTagTyped = optionalEntityTagTyped.get()
            val tagData = tagTyped[remainderFinder()]
            val entityTagData = entityTagTyped.getOrCreate(remainderFinder())
            if (entityTagData["Base"].asNumber().result().isPresent) {
                data = data.set("Damage", data.createShort((entityTagData["Base"].asInt(0) and 15).toShort()))
                val optionalDisplay = tagData["display"].result()
                if (optionalDisplay.isPresent) {
                    val display = optionalDisplay.get()
                    val lore = display.createMap(mapOf(display.createString("Lore") to display.createList(Stream.of(display.createString("(+NBT")))))
                    if (display == lore) return@fixTypeEverywhereTyped typed.set(remainderFinder(), data)
                }
                entityTagData.remove("Base")
                return@fixTypeEverywhereTyped typed.set(remainderFinder(), data).set(tagFinder, tagTyped.set(entityTagFinder, entityTagTyped.set(remainderFinder(), entityTagData)))
            }
            typed.set(remainderFinder(), data)
        }
    }
}
