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
package org.kryptonmc.krypton.util.datafix.fixes.entity

import com.mojang.datafixers.DSL.fieldFinder
import com.mojang.datafixers.DSL.named
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.util.Pair
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.schema.NamespacedSchema

class HorseSaddleFix(outputSchema: Schema, changesType: Boolean) : NamedEntityFix(outputSchema, changesType, "HorseSaddleFix", References.ENTITY, "EntityHorse") {

    override fun fix(typed: Typed<*>): Typed<*> {
        val idFinder = fieldFinder("id", named(References.ITEM_NAME.typeName(), NamespacedSchema.NAMESPACED_STRING))
        val itemType = inputSchema.getTypeRaw(References.ITEM_STACK)
        val saddleFinder = fieldFinder("SaddleItem", itemType)
        val data = typed[remainderFinder()]
        if (typed.getOptionalTyped(saddleFinder).isPresent || !data["Saddle"].asBoolean(false)) return typed
        var saddle = itemType.pointTyped(typed.ops).orElseThrow(::IllegalStateException)
        saddle = saddle.set(idFinder, Pair.of(References.ITEM_NAME.typeName(), "minecraft:saddle"))
        var saddleData = data.emptyMap()
        saddleData = saddleData.set("Count", saddleData.createByte(1))
        saddleData = saddleData.set("Damage", saddleData.createShort(0))
        saddle = saddle.set(remainderFinder(), saddleData)
        data.remove("Saddle")
        return typed.set(saddleFinder, saddle).set(remainderFinder(), data)
    }
}
