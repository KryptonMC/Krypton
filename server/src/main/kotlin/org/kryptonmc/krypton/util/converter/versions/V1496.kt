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
package org.kryptonmc.krypton.util.converter.versions

import ca.spottedleaf.dataconverter.types.MapType
import ca.spottedleaf.dataconverter.types.ObjectType
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import it.unimi.dsi.fastutil.ints.IntConsumer
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import org.kryptonmc.api.util.ceillog2
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.PackedBitStorage
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil
import kotlin.math.max

object V1496 {

    private const val VERSION = MCVersions.V18W21B
    private val DIRECTIONS = arrayOf(
        intArrayOf(-1, 0, 0),
        intArrayOf(1, 0, 0),
        intArrayOf(0, -1, 0),
        intArrayOf(0, 1, 0),
        intArrayOf(0, 0, -1),
        intArrayOf(0, 0, 1)
    )
    private val LEAVES_TO_ID = Object2IntOpenHashMap<String>().apply {
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

    fun register() = MCTypeRegistry.CHUNK.addStructureConverter(VERSION) { data, _, _ ->
        val level = data.getMap<String>("Level") ?: return@addStructureConverter null
        val sectionsNBT = level.getList("Sections", ObjectType.MAP) ?: return@addStructureConverter null

        var newSides = 0
        val sections = arrayOfNulls<LeavesSection>(16)
        var skippable = true
        for (i in 0 until sectionsNBT.size()) {
            val section = LeavesSection(sectionsNBT.getMap(i))
            sections[section.y] = section
            skippable = skippable and section.isSkippable
        }
        if (skippable) return@addStructureConverter null

        val positionsByDistance = Array(7) { IntOpenHashSet() }
        sections.forEach {
            if (it == null || it.isSkippable) return@forEach
            for (index in 0 until 4096) {
                val block = it.getBlock(index)
                if (it.isLog(block)) {
                    positionsByDistance[0].add(it.y shl 12 or index)
                } else if (it.isLeaf(block)) {
                    val x = getX(index)
                    val z = getZ(index)
                    newSides = newSides or getSideMask(x == 0, x == 15, z == 0, z == 15)
                }
            }
        }

        // this is basically supposed to recalculate the distances, because a higher cap was added
        for (distance in 1..6) {
            val positionsLess = positionsByDistance[distance - 1]
            val positionsEqual = positionsByDistance[distance]

            positionsLess.intIterator().forEachRemaining(IntConsumer { position ->
                val fromX = getX(position)
                val fromY = getY(position)
                val fromZ = getZ(position)

                DIRECTIONS.forEach {
                    val toX = fromX + it[0]
                    val toY = fromY + it[1]
                    val toZ = fromZ + it[2]
                    if (toX !in 0..15 || toY !in 0..255 || toZ !in 0..15) return@forEach

                    val toSection = sections[toY shr 4]
                    if (toSection == null || toSection.isSkippable) return@forEach

                    val sectionLocalIndex = indexOf(toX, toY and 15, toZ)
                    val toBlock = toSection.getBlock(sectionLocalIndex)

                    if (toSection.isLeaf(toBlock)) {
                        val newDistance = toSection.getDistance(toBlock)
                        if (newDistance > distance) {
                            toSection.setDistance(sectionLocalIndex, toBlock, distance)
                            positionsEqual.add(indexOf(toX, toY, toZ))
                        }
                    }
                }
            })
        }

        // done updating blocks, now just update the block states and palette
        for (i in 0 until sectionsNBT.size()) {
            val sectionNBT = sectionsNBT.getMap<String>(i)
            val y = sectionNBT.getInt("Y")
            sections[y]?.writeInto(sectionNBT)
        }

        // if sides changed during process, update it now
        if (newSides != 0) {
            val upgradeData = level.getMap("UpgradeData")
                ?: NBTTypeUtil.createEmptyMap<String>().apply { level.setMap("UpgradeData", this) }
            upgradeData.setByte("Sides", (upgradeData.getByte("Sides").toInt() or newSides).toByte())
        }
        null
    }

    fun indexOf(x: Int, y: Int, z: Int) = y shl 8 or (z shl 4) or x

    fun getX(index: Int) = index and 15

    fun getY(index: Int) = index shr 8 and 255

    fun getZ(index: Int) = index shr 4 and 15

    fun getSideMask(noLeft: Boolean, noRight: Boolean, noBack: Boolean, noForward: Boolean): Int {
        return if (noBack) {
            when {
                noRight -> 2
                noLeft -> 128
                else -> 1
            }
        } else if (noForward) {
            when {
                noLeft -> 32
                noRight -> 8
                else -> 16
            }
        } else if (noRight) {
            4
        } else if (noLeft) {
            64
        } else {
            0
        }
    }

    abstract class Section(section: MapType<String>) {

        protected val palette = section.getList("Palette", ObjectType.MAP)!!
        val y = section.getInt("Y")
        protected var storage: PackedBitStorage? = null
        val isSkippable: Boolean
            get() = storage == null

        init {
            readStorage(section)
        }

        protected abstract fun initSkippable(): Boolean

        fun getBlock(index: Int) = storage!![index]

        fun getStateId(name: String, persistent: Boolean, distance: Int) = LEAVES_TO_ID.getInt(name) shl 5 or (if (persistent) 16 else 0) or distance

        fun writeInto(section: MapType<String>) {
            if (isSkippable) return
            section.setList("Palette", palette)
            section.setLongs("BlockStates", storage!!.data)
        }

        private fun readStorage(section: MapType<String>) {
            storage = if (initSkippable()) {
                null
            } else {
                val states = section.getLongs("BlockStates")
                val bits = max(4, palette.size().ceillog2())
                PackedBitStorage(bits, 4096, states)
            }
        }
    }

    class LeavesSection(section: MapType<String>) : Section(section) {

        private var leaveIds: IntOpenHashSet? = null
        private var logIds: IntOpenHashSet? = null
        private var stateToIdMap: Int2IntOpenHashMap? = null

        fun isLog(id: Int) = logIds!!.contains(id)

        fun isLeaf(id: Int) = leaveIds!!.contains(id)

        // only call for logs or leaves, will throw otherwise!
        fun getDistance(id: Int): Int {
            if (isLog(id)) return 0
            return palette.getMap<String>(id).getMap<String>("Properties")!!.getString("distance")!!.toInt()
        }

        fun setDistance(index: Int, id: Int, distance: Int) {
            val state = palette.getMap<String>(id)
            val name = state.getString("Name")
            val persistent = state.getMap<String>("Properties")?.getString("persistent") == "true"
            val newState = getStateId(name!!, persistent, distance)
            var newStateId = stateToIdMap!![newState]
            if (newStateId == -1) {
                newStateId = palette.size()
                leaveIds!!.add(newStateId)
                stateToIdMap!![newState] = newStateId
                palette.addMap(makeNewLeafTag(name, persistent, distance))
            }

            if (newStateId >= 1 shl storage!!.bits) {
                // need to widen storage
                val newStorage = PackedBitStorage(storage!!.bits + 1, 4096)
                for (i in 0 until 4096) {
                    newStorage[i] = storage!![i]
                }
                storage = newStorage
            }
            storage!![index] = newStateId
        }

        override fun initSkippable(): Boolean {
            leaveIds = IntOpenHashSet()
            logIds = IntOpenHashSet()
            stateToIdMap = Int2IntOpenHashMap()
            stateToIdMap!!.defaultReturnValue(-1)

            for (i in 0 until palette.size()) {
                val blockState = palette.getMap<String>(i)
                val name = blockState.getString("Name", "")!!
                if (LEAVES_TO_ID.containsKey(name)) {
                    val properties = blockState.getMap<String>("Properties")
                    val notDecayable = properties != null && properties.getString("decayable") == "false"

                    leaveIds!!.add(i)
                    stateToIdMap!![getStateId(name, notDecayable, 7)] = i
                    palette.setMap(i, makeNewLeafTag(name, notDecayable, 7))
                }
                if (LOGS.contains(name)) logIds!!.add(i)
            }
            return leaveIds!!.isEmpty() && logIds!!.isEmpty()
        }

        private fun makeNewLeafTag(name: String, notDecayable: Boolean, distance: Int): MapType<String> {
            val properties = NBTTypeUtil.createEmptyMap<String>()
            val ret = NBTTypeUtil.createEmptyMap<String>()
            ret.setString("Name", name)
            ret.setMap("Properties", properties)

            properties.setString("persistent", notDecayable.toString())
            properties.setString("distance", distance.toString())
            return ret
        }
    }
}
