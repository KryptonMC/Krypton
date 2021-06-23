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

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType
import com.mojang.datafixers.util.Pair
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Function

class MinecartIdentifiersFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    @Suppress("UNCHECKED_CAST")
    override fun makeRule(): TypeRewriteRule {
        val inputChoiceType = inputSchema.findChoiceType(References.ENTITY) as TaggedChoiceType<String>
        val outputChoiceType = outputSchema.findChoiceType(References.ENTITY) as TaggedChoiceType<String>
        return fixTypeEverywhere("MinecartIdentifiersFix", inputChoiceType, outputChoiceType) { ops ->
            Function { pair ->
                if (pair.first != "Minecart") return@Function pair
                val minecartTyped = inputChoiceType.point(ops, "Minecart", pair.second).orElseThrow(::IllegalStateException)
                val minecart = minecartTyped.getOrCreate(remainderFinder())
                val type = minecart["Type"].asInt(0)
                val name = if (type in MINECART_BY_ID.indices) MINECART_BY_ID[type] else "MinecartRideable"
                Pair.of(name, minecartTyped.write().map { outputChoiceType.types()[name]!!.read(it) }.result().orElseThrow { IllegalStateException("Could not read the new minecart!") })
            }
        }
    }

    companion object {

        private val MINECART_BY_ID = listOf("MinecartRideable", "MinecartChest", "MinecartFurnace")
    }
}
