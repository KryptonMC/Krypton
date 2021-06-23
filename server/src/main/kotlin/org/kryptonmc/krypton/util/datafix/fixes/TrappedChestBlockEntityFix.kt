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
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.List.ListType
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.ints.IntSet
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.getIfPresent

class TrappedChestBlockEntityFix(outputSchema: Schema, changesType: Boolean) : SectionFix(outputSchema, changesType) {

    @Suppress("UNCHECKED_CAST")
    override fun makeRule(): TypeRewriteRule {
        val newChunkType = outputSchema.getType(References.CHUNK)
        val levelType = newChunkType.findFieldType("Level")
        val tileType = levelType.findFieldType("TileEntities")
        check(tileType is ListType<*>) { "Tile entity type is not a list!" }
        val tileFinder = fieldFinder("TileEntities", tileType)
        val chunkType = inputSchema.getType(References.CHUNK)
        val levelFinder = chunkType.findField("Level")
        val sectionsFinder = levelFinder.type().findField("Sections")
        val sectionsType = sectionsFinder.type()
        check(sectionsType is ListType<*>) { "Sections type is not a list!" }
        val sectionType = sectionsType.element
        val sectionFinder = sectionType.finder()
        return TypeRewriteRule.seq(
            AddChoices(outputSchema, "AddTrappedChestFix", References.BLOCK_ENTITY).makeRule(),
            fixTypeEverywhereTyped("Trapped Chest fix", chunkType) { typed ->
                typed.updateTyped(levelFinder) { level ->
                    val sectionsTyped = level.getOptionalTyped(sectionsFinder).getIfPresent() ?: return@updateTyped level
                    val sectionsListTyped = sectionsTyped.getAllTyped(sectionFinder)
                    val sectionIndices = IntOpenHashSet()
                    sectionsListTyped.forEach {
                        val section = TrappedChestSection(it, inputSchema)
                        if (section.isSkippable) return@forEach
                        for (i in 0 until SIZE) {
                            val block = section.storage!![i]
                            if (block in section.chestIds!!) sectionIndices += section.index shl 12 or i
                        }
                    }
                    val chunkData = level[remainderFinder()]
                    val chunkX = chunkData["xPos"].asInt(0)
                    val chunkZ = chunkData["zPos"].asInt(0)
                    val blockEntityChoiceType = inputSchema.findChoiceType(References.BLOCK_ENTITY) as TaggedChoiceType<String>
                    level.updateTyped(tileFinder) { tiles ->
                        tiles.updateTyped(blockEntityChoiceType.finder()) tiles@{ blockEntity ->
                            val data = blockEntity.getOrCreate(remainderFinder())
                            val x = data["x"].asInt(0) - (chunkX shl 4)
                            val y = data["y"].asInt(0)
                            val z = data["z"].asInt(0) - (chunkZ shl 4)
                            if (indexOf(x, y, z) !in sectionIndices) return@tiles blockEntity
                            blockEntity.update(blockEntityChoiceType.finder()) { it.mapFirst { "minecraft:trapped_chest" } }
                        }
                    }
                }
            }
        )
    }

    private class TrappedChestSection(typed: Typed<*>, schema: Schema) : Section(typed, schema) {

        var chestIds: IntSet? = null
            private set

        override val skippable: Boolean
            get() {
                val chestIds = IntOpenHashSet().apply { this@TrappedChestSection.chestIds = this }
                palette.forEachIndexed { index, block ->
                    val name = block["Name"].asString("")
                    if (name == "minecraft:trapped_chest") chestIds += index
                }
                return chestIds.isEmpty()
            }
    }

    companion object {

        private const val SIZE = 4096
    }
}
