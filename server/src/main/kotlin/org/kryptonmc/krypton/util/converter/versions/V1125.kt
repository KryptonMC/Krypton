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

import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.hooks.EnforceNamespacedValueTypeDataHook
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil
import org.kryptonmc.krypton.util.converter.walkers.convertKeys

object V1125 {

    private const val VERSION = MCVersions.V17W15A
    private const val BED_BLOCK_ID = 416

    fun register() {
        MCTypeRegistry.CHUNK.addStructureConverter(VERSION) { data, _, _ ->
            val level = data.getMap<String>("Level") ?: return@addStructureConverter null
            val chunkX = level.getInt("xPos")
            val chunkZ = level.getInt("zPos")
            val sections = level.getList("Sections", ObjectType.MAP) ?: return@addStructureConverter null
            val tileEntities = level.getList("TileEntities", ObjectType.MAP)
                ?: NBTTypeUtil.createEmptyList().apply { level.setList("TileEntities", this) }

            for (i in 0 until sections.size()) {
                val section = sections.getMap<String>(i)
                val sectionY = section.getByte("Y")
                val blocks = section.getBytes("Blocks") ?: continue

                for (blockIndex in blocks.indices) {
                    if ((blocks[blockIndex].toInt() and 255) shl 4 != BED_BLOCK_ID) continue
                    val localX = blockIndex and 15
                    val localZ = (blockIndex shr 4) and 15
                    val localY = (blockIndex shr 8) and 15

                    val newTile = NBTTypeUtil.createEmptyMap<String>()
                    newTile.setString("id", "minecraft:bed")
                    newTile.setInt("x", localX + (chunkX shl 4))
                    newTile.setInt("y", localY + (sectionY.toInt() shl 4))
                    newTile.setInt("z", localZ + (chunkZ shl 4))
                    newTile.setShort("color", 14) // Red
                    tileEntities.addMap(newTile)
                }
            }
            null
        }

        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:bed", VERSION) { data, _, _ ->
            if (data.getShort("Damage") == 0.toShort()) data.setShort("Damage", 14) // Red
            null
        }

        MCTypeRegistry.ADVANCEMENTS.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
            data.getMap<String>("minecraft:adventure/adventuring_time")?.convertKeys(MCTypeRegistry.BIOME, "criteria", fromVersion, toVersion)
            data.getMap<String>("minecraft:adventure/kill_a_mob")?.convertKeys(MCTypeRegistry.ENTITY_NAME, "criteria", fromVersion, toVersion)
            data.getMap<String>("minecraft:adventure/kill_all_mobs")?.convertKeys(MCTypeRegistry.ENTITY_NAME, "criteria", fromVersion, toVersion)
            data.getMap<String>("minecraft:adventure/bred_all_animals")?.convertKeys(MCTypeRegistry.ENTITY_NAME, "criteria", fromVersion, toVersion)
            null
        }

        // Enforce namespacing for ids
        MCTypeRegistry.BIOME.addStructureHook(VERSION, EnforceNamespacedValueTypeDataHook())
    }
}
