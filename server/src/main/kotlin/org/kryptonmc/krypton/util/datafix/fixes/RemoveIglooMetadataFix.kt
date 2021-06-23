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
        return writeFixAndRead("RemoveIglooMetadataFix", inputType, outputType, Dynamic<*>::fixTag)
    }
}

private fun <T> Dynamic<T>.fixTag(): Dynamic<T> {
    val hasChildren = get("Children").asStreamOpt().map { it.allMatch(Dynamic<*>::isIglooPiece) }.result().orElse(false)
    return if (hasChildren) set("id", createString("Igloo")).remove("Children") else update("Children", Dynamic<*>::removeIglooPieces)
}

private fun <T> Dynamic<T>.removeIglooPieces() = asStreamOpt().map { stream -> stream.filter { !it.isIglooPiece() } }
    .map(::createList)
    .result()
    .orElse(this)

private fun Dynamic<*>.isIglooPiece() = get("Id").asString("") == "Iglu"
