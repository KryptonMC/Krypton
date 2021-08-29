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

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.List.ListType
import com.mojang.serialization.Dynamic
import it.unimi.dsi.fastutil.ints.Int2IntMap
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.ints.IntSet
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import org.kryptonmc.krypton.util.datafix.PackedBitStorage
import org.kryptonmc.krypton.util.datafix.References

class LeavesFix(outputSchema: Schema, changesType: Boolean) : SectionFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val chunkType = inputSchema.getType(References.CHUNK)
        val levelFinder = chunkType.findField("Level")
        val sectionsFinder = levelFinder.type().findField("Sections")
        val sectionsType = sectionsFinder.type() as? ListType<*> ?: error("Expecting sections to be of type list!")
        val sectionFinder = sectionsType.element.finder()
        return fixTypeEverywhereTyped("Leaves fix", chunkType) { typed ->
            typed.updateTyped(levelFinder) { level ->
                val array = intArrayOf(0)
                var dataTyped = level.updateTyped(sectionsFinder) sections@{ sections ->
                    val sectionsMap = Int2ObjectOpenHashMap(sections.getAllTyped(sectionFinder).asSequence().map { LeavesSection(it, inputSchema) }.associateBy { it.index })
                    if (sectionsMap.values.all { it.isSkippable }) return@sections sections
                    val intSets = mutableListOf(IntOpenHashSet(), IntOpenHashSet(), IntOpenHashSet(), IntOpenHashSet(), IntOpenHashSet(), IntOpenHashSet(), IntOpenHashSet())
                    sectionsMap.values.forEach {
                        if (it.isSkippable) return@forEach
                        for (i in 0 until SIZE) {
                            val block = it.storage!![i]
                            if (block in it.logIds!!) {
                                intSets[0] += it.index shl 12 or i
                            } else {
                                val x = i and 15
                                val z = i shr 4 and 15
                                array[0] = array[0] or sideMask(x == 0, x == 15, z == 0, z == 15)
                            }
                        }
                    }

                    for (i in 0..6) {
                        val previous = intSets[i - 1]
                        val next = intSets[i]
                        previous.forEach previous@{ int ->
                            val x = int and 15
                            val y = int shr 8 and 255
                            val z = int shr 4 and 15
                            DIRECTIONS.forEach {
                                val directedX = x + it[0]
                                val directedY = y + it[1]
                                val directedZ = z + it[2]
                                if (directedX !in 0..15 || directedY !in 0..255 || directedZ !in 0..15) return@forEach
                                val section = sectionsMap[y shr 4]
                                if (section == null || section.isSkippable) return@forEach
                                val index = indexOf(x, y and 15, z)
                                val blockId = section.storage!![index]
                                if (blockId !in section.leafIds!!) return@forEach
                                val distance = section.distance(blockId)
                                if (distance <= i) return@forEach
                                section.setDistance(index, blockId, i)
                                next += indexOf(directedX, directedY, directedZ)
                            }
                        }
                    }
                    sections.updateTyped(sectionFinder) { sectionsMap[it[remainderFinder()]["Y"].asInt(0)].write(it) }
                }
                if (array[0] != 0) dataTyped = dataTyped.update(remainderFinder()) {
                    val upgradeData = DataFixUtils.orElse(it["UpgradeData"].result(), it.emptyMap())
                    it.set("UpgradeData", upgradeData.set("Sides", it.createByte((upgradeData["Sides"].asByte(0).toInt() or array[0]).toByte())))
                }
                dataTyped
            }
        }
    }

    private class LeavesSection(typed: Typed<*>, schema: Schema) : Section(typed, schema) {

        var leafIds: IntSet? = null
            private set

        var logIds: IntSet? = null
            private set

        private var stateToIdMap: Int2IntMap? = null

        override val skippable: Boolean
            get() {
                val leafIds = IntOpenHashSet().apply { this@LeavesSection.leafIds = this }
                val logIds = IntOpenHashSet().apply { this@LeavesSection.logIds = this }
                val stateToIdMap = Int2IntOpenHashMap().apply { this@LeavesSection.stateToIdMap = this }
                palette.forEachIndexed { index, dynamic ->
                    val name = dynamic["Name"].asString("")
                    if (name in LEAVES) {
                        val isPersistent = dynamic["Properties"][DECAYABLE].asString("") == "false"
                        leafIds += index
                        stateToIdMap[stateId(name, isPersistent, DECAY_DISTANCE)] = index
                        palette[index] = createLeafTag(dynamic, name, isPersistent, DECAY_DISTANCE)
                    }
                    if (name in LOGS) logIds += index
                }
                return leafIds.isEmpty() && logIds.isEmpty()
            }

        private fun stateId(name: String, isPersistent: Boolean, decayDistance: Int) = LEAVES.getInt(name) shl 5 or (if (isPersistent) 16 else 0) or decayDistance

        private fun createLeafTag(dynamic: Dynamic<*>, name: String, isPersistent: Boolean, decayDistance: Int): Dynamic<*> {
            var properties = dynamic.emptyMap()
            properties = properties.set(PERSISTENT, properties.createString(isPersistent.toString()))
            properties = properties.set(DISTANCE, properties.createString(decayDistance.toString()))
            var data = dynamic.emptyMap()
            data = data.set("Properties", properties)
            return data.set("Name", data.createString(name))
        }

        fun distance(index: Int) = if (index in logIds!!) 0 else palette[index]["Properties"][DISTANCE].asString("").toInt()

        fun setDistance(index: Int, blockId: Int, decayDistance: Int) {
            val dynamic = palette[blockId]
            val name = dynamic["Name"].asString("")
            val isPersistent = dynamic["Properties"][PERSISTENT].asString("") == "true"
            val stateId = stateId(name, isPersistent, decayDistance)
            if (stateId !in stateToIdMap!!) {
                val paletteSize = palette.size
                leafIds!! += paletteSize
                stateToIdMap!![stateId] = paletteSize
                palette += createLeafTag(dynamic, name, isPersistent, decayDistance)
            }
            val id = stateToIdMap!![stateId]
            if (1 shl storage!!.bits <= id) {
                val newStorage = PackedBitStorage(storage!!.bits + 1, SIZE)
                for (i in 0 until SIZE) newStorage[i] = storage!![i]
                storage = newStorage
            }
            storage!![index] = id
        }

        companion object {

            private const val PERSISTENT = "persistent"
            private const val DECAYABLE = "decayable"
            private const val DISTANCE = "distance"
        }
    }

    companion object {

        private const val DECAY_DISTANCE = 7
        private const val SIZE = 4096

        private val DIRECTIONS = arrayOf(intArrayOf(-1, 0, 0), intArrayOf(1, 0, 0), intArrayOf(0, -1, 0), intArrayOf(0, 1, 0), intArrayOf(0, 0, -1), intArrayOf(0, 0, 1))
        private val LEAVES = Object2IntOpenHashMap<String>().apply {
            put("minecraft:acacia_leaves", 0)
            put("minecraft:birch_leaves", 1)
            put("minecraft:dark_oak_leaves", 2)
            put("minecraft:jungle_leaves", 3)
            put("minecraft:oak_leaves", 4)
            put("minecraft:spruce_leaves", 5)
        }
        private val LOGS = setOf(
            "minecraft:acacia_bark",
            "minecraft:birch_bark",
            "minecraft:dark_oak_bark",
            "minecraft:jungle_bark",
            "minecraft:oak_bark",
            "minecraft:spruce_bark",
            "minecraft:acacia_log",
            "minecraft:birch_log",
            "minecraft:dark_oak_log",
            "minecraft:jungle_log",
            "minecraft:oak_log",
            "minecraft:spruce_log",
            "minecraft:stripped_acacia_log",
            "minecraft:stripped_birch_log",
            "minecraft:stripped_dark_oak_log",
            "minecraft:stripped_jungle_log",
            "minecraft:stripped_oak_log",
            "minecraft:stripped_spruce_log"
        )

        private fun indexOf(x: Int, y: Int, z: Int) = y shl 8 or (z shl 4) or x
    }
}
