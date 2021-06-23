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
package org.kryptonmc.krypton.util.datafix.fixes.uuid

import com.mojang.datafixers.DSL.fieldFinder
import com.mojang.datafixers.DSL.named
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.schema.NamespacedSchema

class ItemStackUUIDFix(outputSchema: Schema) : UUIDFix(outputSchema, References.ITEM_STACK) {

    override fun makeRule(): TypeRewriteRule {
        val idFinder = fieldFinder("id", named(References.ITEM_NAME.typeName(), NamespacedSchema.NAMESPACED_STRING))
        return fixTypeEverywhereTyped("ItemStackUUIDFix", inputSchema.getType(typeReference)) { typed ->
            typed.updateTyped(typed.type.findField("tag")) { tagTyped ->
                tagTyped.update(remainderFinder()) { tag ->
                    var temp = tag.updateAttributeModifiers()
                    if (typed.getOptional(idFinder).map { it.second == "minecraft:player_head" }.orElse(false)) temp = temp.updateSkullOwner()
                    temp
                }
            }
        }
    }

    private fun Dynamic<*>.updateAttributeModifiers() = update("AttributeModifiers") { modifiers ->
        createList(modifiers.asStream().map { it.replaceUUIDLeastMost("UUID", "UUID").orElse(it) })
    }

    private fun Dynamic<*>.updateSkullOwner() = update("SkullOwner") { it.replaceUUIDString("Id", "Id").orElse(it) }
}
