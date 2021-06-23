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
package org.kryptonmc.krypton.util.datafix.fixes

import com.mojang.datafixers.DSL.fieldFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import org.kryptonmc.krypton.util.datafix.References

abstract class AbstractChunkFix(private val name: String, outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    abstract fun fix(level: Typed<*>): Typed<*>

    override fun makeRule(): TypeRewriteRule {
        val type = inputSchema.getType(References.CHUNK)
        val levelType = type.findFieldType("Level")
        val levelFinder = fieldFinder("Level", levelType)
        return fixTypeEverywhereTyped(name, type, outputSchema.getType(References.CHUNK)) { typed -> typed.updateTyped(levelFinder) { fix(it) } }
    }
}
