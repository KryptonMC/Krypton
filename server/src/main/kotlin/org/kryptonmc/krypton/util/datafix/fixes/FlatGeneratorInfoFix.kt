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

import com.google.common.base.Splitter
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.fixes.BlockStateData.tag
import org.kryptonmc.krypton.util.datafix.fixes.entity.EntityBlockStateFix.Companion.toBlockID

class FlatGeneratorInfoFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule = fixTypeEverywhereTyped("FlatGeneratorInfoFix", inputSchema.getType(References.LEVEL)) { typed ->
        typed.update(remainderFinder()) { it.fix() }
    }

    private fun Dynamic<*>.fix(): Dynamic<*> = if (get("generatorName").asString("").lowercase() == "flat") update(GENERATOR_OPTIONS) { options ->
        DataFixUtils.orElse(options.asString().map { it.fix() }.map(options::createString).result(), options)
    } else this

    private fun String.fix(): String {
        if (isEmpty()) return DEFAULT
        val iterator = SPLITTER.split(this).iterator()
        val first = iterator.next()
        val version = if (iterator.hasNext()) first.toIntOrNull() ?: 0 else 0
        val next = if (iterator.hasNext()) iterator.next() else first
        if (version !in 0..3) return DEFAULT
        val builder = StringBuilder()
        val splitter = if (version < 3) OLD_AMOUNT_SPLITTER else AMOUNT_SPLITTER
        builder.append(LAYER_SPLITTER.split(next).asSequence().map {
            val amounts = splitter.splitToList(it)
            val amount = if (amounts.size == 2) amounts[0].toIntOrNull() ?: 0 else 1
            val blocksString = if (amounts.size == 2) amounts[1] else amounts[0]
            val blocks = BLOCK_SPLITTER.splitToList(blocksString)
            val index = if (blocks.first() == "minecraft") 1 else 0
            val block = blocks[index]
            val blockId = if (version == 3) "minecraft:$block".toBlockID() else block.toIntOrNull() ?: 0
            val nextIndex = index + 1
            val id = blocks.getOrNull(nextIndex)?.toIntOrNull() ?: 0
            (if (amount == 1) "" else "$amount*") + (blockId shl 4 or id).tag()!!["Name"].asString("")
        })
        while (iterator.hasNext()) builder.append(";").append(iterator.next())
        return builder.toString()
    }

    companion object {

        private const val GENERATOR_OPTIONS = "generatorOptions"
        private const val DEFAULT = "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village"
        private val SPLITTER = Splitter.on(";").limit(5)
        private val LAYER_SPLITTER = Splitter.on(",")
        private val OLD_AMOUNT_SPLITTER = Splitter.on("x").limit(2)
        private val AMOUNT_SPLITTER = Splitter.on("*").limit(2)
        private val BLOCK_SPLITTER = Splitter.on(":").limit(3)
    }
}
