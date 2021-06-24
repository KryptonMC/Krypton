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

import com.mojang.datafixers.DSL
import com.mojang.datafixers.DSL.namedChoice
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema

abstract class NamedEntityFix(
    outputSchema: Schema,
    changesType: Boolean,
    private val name: String,
    private val type: DSL.TypeReference,
    private val entityName: String
) : DataFix(outputSchema, changesType) {

    abstract fun fix(typed: Typed<*>): Typed<*>

    override fun makeRule(): TypeRewriteRule {
        val entityNameFinder = namedChoice(entityName, inputSchema.getChoiceType(type, entityName))
        return fixTypeEverywhereTyped(name, inputSchema.getType(type), outputSchema.getType(type)) {
            it.updateTyped(entityNameFinder, outputSchema.getChoiceType(type, entityName), ::fix)
        }
    }
}
