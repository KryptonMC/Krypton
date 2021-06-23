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

import com.mojang.datafixers.DSL.named
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.schema.NamespacedSchema
import java.util.function.Function

sealed class RenameEntityFix(private val name: String, outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    protected abstract fun rename(name: String): String

    @Suppress("UNCHECKED_CAST")
    override fun makeRule(): TypeRewriteRule {
        val inputChoiceType = inputSchema.findChoiceType(References.ENTITY) as TaggedChoiceType<String>
        val outputChoiceType = outputSchema.findChoiceType(References.ENTITY) as TaggedChoiceType<String>
        val nameType = named(References.ENTITY_NAME.typeName(), NamespacedSchema.NAMESPACED_STRING)
        check(outputSchema.getType(References.ENTITY_NAME) == nameType) { "Entity name type is incorrect! Expected $nameType, got ${outputSchema.getType(References.ENTITY_NAME)}!" }
        return TypeRewriteRule.seq(
            fixTypeEverywhere(name, inputChoiceType, outputChoiceType) {
                Function { pair ->
                    pair.mapFirst {
                        val renamed = rename(it)
                        val type = inputChoiceType.types()[it]!!
                        val renamedType = outputChoiceType.types()[renamed]!!
                        check(renamedType.equals(type, true, true)) { "Failed dynamic type check! $renamedType is not equal to $type!" }
                        renamed
                    }
                }
            },
            fixTypeEverywhere("$name for entity name", nameType) { Function { it.mapSecond(::rename) } }
        )
    }
}
