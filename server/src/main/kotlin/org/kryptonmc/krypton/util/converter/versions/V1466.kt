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

import ca.spottedleaf.dataconverter.types.ListType
import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil
import org.kryptonmc.krypton.util.converter.walkers.convert
import org.kryptonmc.krypton.util.converter.walkers.convertList
import org.kryptonmc.krypton.util.converter.walkers.convertValues
import kotlin.math.min

object V1466 {

    private const val VERSION = MCVersions.V18W06A

    fun register() {
        MCTypeRegistry.CHUNK.addStructureConverter(VERSION) { data, _, _ ->
            val level = data.getMap<String>("Level") ?: return@addStructureConverter null
            val terrainPopulated = level.getBoolean("TerrainPopulated")
            val lightPopulated = level.getBoolean("LightPopulated") || level.getNumber("LightPopulated") == null
            val newStatus = if (!terrainPopulated) "carved" else if (lightPopulated) "mobs_spawned" else "decorated"

            level.setString("Status", newStatus)
            level.setBoolean("hasLegacyStructureData", true)

            // convert biome byte[] into int[]
            val biomes = level.getBytes("Biomes")
            if (biomes != null) {
                val newBiomes = IntArray(256)
                for (i in 0 until min(newBiomes.size, biomes.size)) {
                    newBiomes[i] = biomes[i].toInt() and 255
                }
                level.setInts("Biomes", newBiomes)
            }

            // ProtoChunks have their own dedicated tick list, so we must convert the TileTicks to that.
            level.getList("TileTicks", ObjectType.MAP)?.let {
                val sections = NBTTypeUtil.createEmptyList()
                val sectionAccess = Array<ListType>(16) { NBTTypeUtil.createEmptyList() }
                for (i in sectionAccess.indices) {
                    sections.addList(sectionAccess[i])
                }
                level.setList("ToBeTicked", sections)

                for (i in 0 until it.size()) {
                    val tick = it.getMap<String>(i)
                    val x = tick.getInt("x")
                    val y = tick.getInt("y")
                    val z = tick.getInt("z")
                    val coordinate = packOffsetCoordinates(x, y, z)
                    sectionAccess[y shr 4].addShort(coordinate)
                }
            }
            null
        }

        MCTypeRegistry.CHUNK.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
            val level = data.getMap<String>("Level") ?: return@addStructureWalker null
            level.convertList(MCTypeRegistry.ENTITY, "Entities", fromVersion, toVersion)
            level.convertList(MCTypeRegistry.TILE_ENTITY, "TileEntities", fromVersion, toVersion)

            level.getList("TileTicks", ObjectType.MAP)?.let {
                for (i in 0 until it.size()) {
                    it.getMap<String>(i).convert(MCTypeRegistry.BLOCK_NAME, "i", fromVersion, toVersion)
                }
            }

            level.getList("Sections", ObjectType.MAP)?.let {
                for (i in 0 until it.size()) {
                    it.getMap<String>(i).convertList(MCTypeRegistry.BLOCK_STATE, "Palette", fromVersion, toVersion)
                }
            }

            level.getMap<String>("Structures")
                ?.convertValues(MCTypeRegistry.STRUCTURE_FEATURE, "Starts", fromVersion, toVersion)
            null
        }

        MCTypeRegistry.STRUCTURE_FEATURE.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
            data.getList("Children", ObjectType.MAP)?.let {
                for (i in 0 until it.size()) {
                    val child = it.getMap<String>(i)
                    child.convert(MCTypeRegistry.BLOCK_STATE, "CA", fromVersion, toVersion)
                    child.convert(MCTypeRegistry.BLOCK_STATE, "CB", fromVersion, toVersion)
                    child.convert(MCTypeRegistry.BLOCK_STATE, "CC", fromVersion, toVersion)
                    child.convert(MCTypeRegistry.BLOCK_STATE, "CD", fromVersion, toVersion)
                }
            }
            data.convert(MCTypeRegistry.BIOME, "biome", fromVersion, toVersion)
            null
        }
    }

    private fun packOffsetCoordinates(x: Int, y: Int, z: Int) =
        ((x and 15) or ((y and 15) shl 4) or ((z and 15) shl 8)).toShort()
}
