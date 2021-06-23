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

import com.mojang.datafixers.DSL.and
import com.mojang.datafixers.DSL.field
import com.mojang.datafixers.DSL.fieldFinder
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optional
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DSL.remainderType
import com.mojang.datafixers.DSL.unit
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.Type
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import com.mojang.datafixers.util.Unit
import org.kryptonmc.krypton.util.datafix.References
import java.util.stream.Stream
import kotlin.math.min

class SplitEquipmentFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule() = cap(inputSchema.getTypeRaw(References.ITEM_STACK))

    private fun <IS> cap(itemType: Type<IS>): TypeRewriteRule {
        val equipmentType = and(optional(field("Equipment", list(itemType))), remainderType())
        val newType = and(optional(field("ArmorItems", list(itemType))), optional(field("HandItems", list(itemType))), remainderType())
        return fixTypeEverywhereTyped("SplitEquipmentFix", inputSchema.getType(References.ENTITY), outputSchema.getType(References.ENTITY)) { typed ->
            var handItems = Either.right<List<IS>, Unit>(unit())
            var armorItems = Either.right<List<IS>, Unit>(unit())
            var data = typed.getOrCreate(remainderFinder())
            val optionalEquipment = typed.getOptional(fieldFinder("Equipment", list(itemType)))
            if (optionalEquipment.isPresent) {
                val equipment = optionalEquipment.get()
                val stack = itemType.read(data.emptyMap()).result().orElseThrow { IllegalStateException("Could not parse newly created empty item stack!") }.first
                if (equipment.isNotEmpty()) handItems = Either.left(listOf(equipment[0], stack))
                if (equipment.size > 1) {
                    val armor = mutableListOf(stack, stack, stack, stack)
                    for (i in 1 until min(equipment.size, 5)) armor[i - 1] = equipment[i]
                    armorItems = Either.left(armor)
                }
            }
            val dropChances = data["DropChances"].asStreamOpt().result()
            if (dropChances.isPresent) {
                val iterator = Stream.concat(dropChances.get(), Stream.generate { data.createInt(0) }).iterator()
                val first = iterator.next().asFloat(0F)
                if (data["HandDropChances"].result().isEmpty) {
                    val stream = Stream.of(first, 0F)
                    data = data.set("HandDropChances", data.createList(stream.map(data::createFloat)))
                }
                if (data["ArmorDropChances"].result().isEmpty) {
                    val stream = Stream.of(iterator.next().asFloat(0F), iterator.next().asFloat(0F), iterator.next().asFloat(0F), iterator.next().asFloat(0F))
                    data = data.set("ArmorDropChances", data.createList(stream.map(data::createFloat)))
                }
                data = data.remove("DropChances")
            }
            typed.set(equipmentType.finder(), newType, Pair.of(handItems, Pair.of(armorItems, data)))
        }
    }
}
