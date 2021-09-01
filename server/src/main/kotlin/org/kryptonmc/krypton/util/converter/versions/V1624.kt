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
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.logger

object V1624 {

    private val LOGGER = logger<V1624>()
    private const val VERSION = MCVersions.V18W32A + 1

    fun register() = MCTypeRegistry.CHUNK.addStructureConverter(VERSION) { data, _, _ ->
        val level = data.getMap<String>("Level") ?: return@addStructureConverter null
        val sections = level.getList("Sections", ObjectType.MAP) ?: return@addStructureConverter null

        val positionsToLook = IntOpenHashSet()
        for (i in 0 until sections.size()) {
            val section = TrappedChestSection(sections.getMap(i))
            if (section.isSkippable) continue
            for (index in 0 until 4096) {
                if (section.isTrappedChest(section.getBlock(index))) positionsToLook.add(section.y shl 12 or index)
            }
        }

        val chunkX = level.getInt("xPos")
        val chunkZ = level.getInt("zPos")
        level.getList("TileEntities", ObjectType.MAP)?.let {
            for (i in 0 until it.size()) {
                val tile = it.getMap<String>(i)
                val x = tile.getInt("x")
                val y = tile.getInt("y")
                val z = tile.getInt("z")

                val index = V1496.indexOf(x - (chunkX shl 4), y, z - (chunkZ shl 4))
                if (!positionsToLook.contains(index)) continue
                val id = tile.getString("id")
                if (id != "minecraft:chest") LOGGER.warn("Block Entity ($x, $y, $z) was expected to be a chest (V1624)")
                tile.setString("id", "minecraft:trapped_chest")
            }
        }
        null
    }

    class TrappedChestSection(section: MapType<String>) : V1496.Section(section) {

        private var chestIds: IntOpenHashSet? = null

        fun isTrappedChest(id: Int) = chestIds!!.contains(id)

        override fun initSkippable(): Boolean {
            chestIds = IntOpenHashSet()
            for (i in 0 until palette.size()) {
                val blockState = palette.getMap<String>(i)
                val name = blockState.getString("Name")
                if (name == "minecraft:trapped_chest") chestIds!!.add(i)
            }
            return chestIds!!.isEmpty()
        }
    }
}
