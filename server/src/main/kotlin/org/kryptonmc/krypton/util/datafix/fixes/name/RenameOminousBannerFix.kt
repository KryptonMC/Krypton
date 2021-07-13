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

import com.mojang.datafixers.DSL.fieldFinder
import com.mojang.datafixers.DSL.named
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.kryptonmc.api.util.getIfPresent
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.schema.NamespacedSchema

class RenameOminousBannerFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val itemType = inputSchema.getType(References.ITEM_STACK)
        val idFinder = fieldFinder("id", named(References.ITEM_NAME.typeName(), NamespacedSchema.NAMESPACED_STRING))
        val tagFinder = itemType.findField("tag")
        return fixTypeEverywhereTyped("RenameOminousBannerFix", itemType) {
            val id = it.getOptional(idFinder)
            if (id.isEmpty || id.get().second != "minecraft:white_banner") return@fixTypeEverywhereTyped it
            val tag = it.getOptionalTyped(tagFinder).getIfPresent() ?: return@fixTypeEverywhereTyped it
            val tagData = tag[remainderFinder()]
            it.set(tagFinder, tag.set(remainderFinder(), tagData.fixTag()))
        }
    }

    private fun Dynamic<*>.fixTag(): Dynamic<*> {
        val optionalDisplay = get("display").result()
        if (optionalDisplay.isEmpty) return this
        var display = optionalDisplay.get()
        val optionalName = display["Name"].asString().result()
        if (optionalName.isPresent) {
            val name = optionalName.get().replace("\"translate\":\"block.minecraft.illager_banner\"", "\"translate\":\"block.minecraft.ominous_banner\"")
            display = display.set("Name", display.createString(name))
        }
        return set("display", display)
    }
}
