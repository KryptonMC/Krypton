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
import org.kryptonmc.krypton.util.datafix.fixes.BlockStateData.tag
import java.util.stream.Stream

class VillageCropFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule = writeFixAndRead(
        "VillageCropFix",
        inputSchema.getType(References.STRUCTURE_FEATURE),
        outputSchema.getType(References.STRUCTURE_FEATURE)
    ) { typed -> typed.update("Children") { it.updateChildren() } }

    companion object {

        private fun <T> Dynamic<T>.updateChildren() = asStreamOpt().map { it.updateChildren() }
            .map(::createList)
            .result()
            .orElse(this)

        private fun Stream<out Dynamic<*>>.updateChildren() = map {
            when (it["id"].asString("")) {
                "ViF" -> it.updateSingleField()
                "ViDF" -> it.updateDoubleField()
                else -> it
            }
        }

        private fun <T> Dynamic<T>.updateSingleField(): Dynamic<T> {
            val temp = updateCrop("CA")
            return temp.updateCrop("CB")
        }

        private fun <T> Dynamic<T>.updateDoubleField(): Dynamic<T> {
            var temp = updateCrop("CA")
            temp = temp.updateCrop("CB")
            temp = temp.updateCrop("CC")
            return temp.updateCrop("CD")
        }

        private fun <T> Dynamic<T>.updateCrop(name: String) = if (get(name).asNumber().result().isPresent) {
            set(name, (get(name).asInt(0) shr 4).tag())
        } else {
            this
        }
    }
}
