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
package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.constType
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.DSL.string
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

fun Schema.equipment(): TypeTemplate = optionalFields(
    "ArmorItems", list(References.ITEM_STACK.`in`(this)),
    "HandItems", list(References.ITEM_STACK.`in`(this))
)

fun Schema.registerItem(map: Map<String, Supplier<TypeTemplate>>, name: String) = register(map, name) { _ ->
    optionalFields("Item", References.ITEM_STACK.`in`(this))
}

fun Schema.registerMob(map: Map<String, Supplier<TypeTemplate>>, name: String) = register(map, name) { _ -> equipment() }

fun Schema.registerProjectile(map: Map<String, Supplier<TypeTemplate>>, name: String) = register(map, name) { _ ->
    optionalFields("inTile", References.BLOCK_NAME.`in`(this))
}

fun Schema.registerMinecart(map: Map<String, Supplier<TypeTemplate>>, name: String) = register(map, name) { _ ->
    optionalFields("DisplayTile", References.BLOCK_NAME.`in`(this))
}

fun Schema.registerContainer(map: Map<String, Supplier<TypeTemplate>>, name: String) = register(map, name) { _ ->
    optionalFields("Items", list(References.ITEM_STACK.`in`(this)))
}

fun Schema.createCriterionTypes(): Map<String, Supplier<TypeTemplate>> {
    val item = Supplier { optionalFields("id", References.ITEM_NAME.`in`(this)) }
    val block = Supplier { optionalFields("id", References.BLOCK_NAME.`in`(this)) }
    val entity = Supplier { optionalFields("id", References.ENTITY_NAME.`in`(this)) }
    return mapOf(
        "minecraft:mined" to block,
        "minecraft:crafted" to item,
        "minecraft:used" to item,
        "minecraft:broken" to item,
        "minecraft:picked_up" to item,
        "minecraft:dropped" to item,
        "minecraft:killed" to entity,
        "minecraft:killed_by" to entity,
        "minecraft:custom" to Supplier { optionalFields("id", constType(NamespacedSchema.NAMESPACED_STRING)) },
        "_special" to Supplier { optionalFields("id", constType(string())) }
    )
}

fun <T> Dynamic<T>.addNames(names: Map<String, String>, armorStandId: String): T = update("tag") { dynamic ->
    dynamic.update("BlockEntityTag") { dynamic1 ->
        val blockId = dynamic1["id"].asString().result().map(String::ensureNamespaced).orElse("minecraft:air")
        if (blockId == "minecraft:air") dynamic1 else names[blockId]?.let { dynamic1.set("id", createString(it)) }
    }.update("EntityTag") {
        if (it["id"].asString("").ensureNamespaced() == "minecraft:armor_stand") it.set("id", createString(armorStandId)) else it
    }
}.value
