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

import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.Type
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.DynamicOps
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Function

sealed class SplitEntityFix(private val name: String, outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    protected abstract fun fix(entityName: String, typed: Typed<*>): Pair<String, Typed<*>>

    @Suppress("UNCHECKED_CAST")
    override fun makeRule(): TypeRewriteRule {
        val inputChoiceType = inputSchema.findChoiceType(References.ENTITY) as TaggedChoiceType<String>
        val outputChoiceType = outputSchema.findChoiceType(References.ENTITY) as TaggedChoiceType<String>
        return fixTypeEverywhere(name, inputChoiceType, outputChoiceType) { ops ->
            Function {
                val typeKey = it.first
                val type = inputChoiceType.types()[typeKey]!!
                val fixed = fix(typeKey, entity(it.second, ops, type))
                val newType = outputChoiceType.types()[fixed.first]!!
                check(newType.equals(fixed.second.type, true, true)) { "Failed dynamic type check! $newType is not equal to ${fixed.second.type}!" }
                Pair.of(fixed.first, fixed.second.value)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <A> entity(value: Any, ops: DynamicOps<*>, type: Type<A>) = Typed(type, ops, value as A)
}
