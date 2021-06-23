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

import com.mojang.datafixers.DSL.compoundList
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DSL.string
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.CompoundList.CompoundListType
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.schema.ensureNamespaced

class NewVillageFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val type = compoundList(string(), inputSchema.getType(References.STRUCTURE_FEATURE))
        return cap(type)
    }

    private fun <S> cap(type: CompoundListType<String, S>): TypeRewriteRule {
        val chunkType = inputSchema.getType(References.CHUNK)
        val structureType = inputSchema.getType(References.STRUCTURE_FEATURE)
        val levelFinder = chunkType.findField("Level")
        val structuresFinder = levelFinder.type().findField("Structures")
        val startsFinder = structuresFinder.type().findField("Starts")
        val finder = type.finder()
        return TypeRewriteRule.seq(
            fixTypeEverywhereTyped("NewVillageFix", chunkType) { typed ->
                typed.updateTyped(levelFinder) { level ->
                    level.updateTyped(structuresFinder) { structures ->
                        structures.updateTyped(startsFinder) { starts ->
                            starts.update(finder) { list -> list.asSequence()
                                .filter { it.first != "Village" }
                                .map { pair -> pair.mapFirst { if (it == "New_Village") "Village" else it } }
                                .toList() }
                        }.update(remainderFinder()) { dynamic ->
                            dynamic.update("References") { references ->
                                val newVillage = references["New_Village"].result()
                                DataFixUtils.orElse(newVillage.map { references.remove("New_Village").set("Village", it) }, references).remove("Village")
                            }
                        }
                    }
                }
            },
            fixTypeEverywhereTyped("NewVillageStartFix", structureType) { typed ->
                typed.update(remainderFinder()) { starts ->
                    starts.update("id") { if (it.asString("").ensureNamespaced() == "minecraft:new_village") it.createString("minecraft:village") else it }
                }
            }
        )
    }
}
