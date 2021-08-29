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

import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References

class RemoveIglooMetadataFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val inputType = inputSchema.getType(References.STRUCTURE_FEATURE)
        val outputType = outputSchema.getType(References.STRUCTURE_FEATURE)
        return writeFixAndRead("RemoveIglooMetadataFix", inputType, outputType) { fixTag(it) }
    }

    companion object {

        private fun <T> fixTag(data: Dynamic<T>): Dynamic<T> {
            val hasChildren = data["Children"].asStreamOpt().map { it.allMatch(::isIglooPiece) }
                .result()
                .orElse(false)
            return if (hasChildren) {
                data.set("id", data.createString("Igloo")).remove("Children")
            } else {
                data.update("Children") { removeIglooPieces(it) }
            }
        }

        private fun <T> removeIglooPieces(data: Dynamic<T>) = data.asStreamOpt()
            .map { stream -> stream.filter { !isIglooPiece(data) } }
            .map(data::createList)
            .result()
            .orElse(data)

        private fun isIglooPiece(data: Dynamic<*>) = data["Id"].asString("") == "Iglu"
    }
}
