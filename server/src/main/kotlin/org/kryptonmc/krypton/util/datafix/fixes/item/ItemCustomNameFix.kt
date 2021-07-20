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
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.toJsonString
import org.kryptonmc.krypton.util.datafix.References

class ItemCustomNameFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val itemType = inputSchema.getType(References.ITEM_STACK)
        val tagFinder = itemType.findField("tag")
        return fixTypeEverywhereTyped("ItemCustomNameFix", itemType) { typed -> typed.updateTyped(tagFinder) { tag -> tag.update(remainderFinder()) { it.fixTag() } } }
    }

    private fun Dynamic<*>.fixTag(): Dynamic<*> {
        val optionalDisplay = get("display").result()
        if (optionalDisplay.isEmpty) return this
        var display = optionalDisplay.get()
        val name = display["Name"].asString().result()
        if (name.isPresent) {
            display = display.set("Name", display.createString(Component.text(name.get()).toJsonString()))
        } else {
            val locName = display["LocName"].asString().result()
            if (locName.isPresent) {
                display = display.set("Name", display.createString(Component.translatable(locName.get()).toJsonString()))
                display = display.remove("LocName")
            }
        }
        return set("display", display)
    }
}
