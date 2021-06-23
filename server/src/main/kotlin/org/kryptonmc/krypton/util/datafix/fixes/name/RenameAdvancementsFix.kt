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

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import org.kryptonmc.krypton.util.datafix.References

open class RenameAdvancementsFix(
    outputSchema: Schema,
    changesType: Boolean,
    private val name: String,
    private val renamer: (String) -> String
) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule = fixTypeEverywhereTyped(name, inputSchema.getType(References.ADVANCEMENTS)) { typed ->
        typed.update(remainderFinder()) { map ->
            map.updateMapValues { values ->
                val string = values.first.asString("")
                values.mapFirst { map.createString(renamer(string)) }
            }
        }
    }
}
