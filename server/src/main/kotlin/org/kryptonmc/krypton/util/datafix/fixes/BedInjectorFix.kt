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
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.Type
import com.mojang.datafixers.types.templates.List.ListType
import com.mojang.datafixers.types.templates.TaggedChoice
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Function

class BedInjectorFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val chunkType = outputSchema.getType(References.CHUNK)
        val levelType = chunkType.findFieldType("Level")
        val blockEntityListType = levelType.findFieldType("TileEntities")
        check(blockEntityListType is ListType<*>) { "Type of block entity list $blockEntityListType is not a valid list type!" }
        return levelType.cap(blockEntityListType)
    }

    @Suppress("UNCHECKED_CAST") // As with AddChoices, not much we can really do here
    private fun <T> Type<*>.cap(blockEntityListType: ListType<T>): TypeRewriteRule {
        val element = blockEntityListType.element
        val levelFinder = fieldFinder("Level", this)
        val blockEntityFinder = fieldFinder("TileEntities", blockEntityListType)
        return TypeRewriteRule.seq(
            fixTypeEverywhere(
                "InjectBedBlockEntityType",
                inputSchema.findChoiceType(References.BLOCK_ENTITY) as TaggedChoice.TaggedChoiceType<Any>,
                outputSchema.findChoiceType(References.BLOCK_ENTITY) as TaggedChoice.TaggedChoiceType<Any>
            ) { Function { it } },
            fixTypeEverywhereTyped("BedBlockEntityInjectorFix", outputSchema.getType(References.CHUNK)) { typed ->
                val levelTyped = typed.getTyped(levelFinder)
                val level = levelTyped[remainderFinder()]
                val xPos = level["xPos"].asInt(0)
                val zPos = level["zPos"].asInt(0)
                val blockEntities = ArrayList(levelTyped.getOrCreate(blockEntityFinder))
                val sections = level["Sections"].asList { it }

                sections.forEach { section ->
                    val y = section["Y"].asInt(0)
                    val blocks = section["Blocks"].asStream().map { it.asInt(0) }
                    var i = 0
                    blocks.forEach blocks@{
                        if ((it and 255) shl 4 != 416) {
                            i++
                            return@blocks
                        }
                        val and = i and 15
                        val shift = i shr 8 and 15
                        val otherShift = i shr 4 and 15
                        val map = mapOf(
                            section.createString("id") to section.createString("minecraft:bed"),
                            section.createString("x") to section.createInt(and + (xPos shl 4)),
                            section.createString("y") to section.createInt(shift + (y shl 4)),
                            section.createString("z") to section.createInt(otherShift + (zPos shl 4)),
                            section.createString("color") to section.createShort(14)
                        )
                        blockEntities += element.read(section.createMap(map)).result().orElseThrow { IllegalStateException("Could not parse newly created bed block entity.") }.first
                        i++
                    }
                }

                if (blockEntities.isNotEmpty()) typed.set(levelFinder, levelTyped.set(blockEntityFinder, blockEntities)) else typed
            }
        )
    }
}
