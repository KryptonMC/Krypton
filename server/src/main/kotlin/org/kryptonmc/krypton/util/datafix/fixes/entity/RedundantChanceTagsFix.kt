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
import com.mojang.serialization.Codec
import com.mojang.serialization.OptionalDynamic
import org.kryptonmc.krypton.util.datafix.References

class RedundantChanceTagsFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule = fixTypeEverywhereTyped(
        "RedundantChanceTagsFix",
        inputSchema.getType(References.ENTITY)
    ) { typed ->
        typed.update(remainderFinder()) {
            var temp = it
            if (it["HandDropChances"].isZeroList(2)) temp = temp.remove("HandDropChances")
            if (it["ArmorDropChances"].isZeroList(4)) temp = temp.remove("ArmorDropChances")
            temp
        }
    }

    companion object {

        private val FLOAT_LIST_CODEC = Codec.FLOAT.listOf()

        private fun OptionalDynamic<*>.isZeroList(size: Int) = flatMap { FLOAT_LIST_CODEC.parse(it) }.map { list ->
            list.size == size && list.all { it == 0F }
        }.result().orElse(false)
    }
}
