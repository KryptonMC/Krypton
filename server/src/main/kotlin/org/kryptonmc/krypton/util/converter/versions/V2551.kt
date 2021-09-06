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
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.walkers.convert
import org.kryptonmc.krypton.util.converter.walkers.convertList

object V2551 {

    private const val VERSION = MCVersions.V20W20B + 14

    fun register() = MCTypeRegistry.WORLD_GEN_SETTINGS.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
        val dimensions = data.getMap<String>("dimensions") ?: return@addStructureWalker null
        dimensions.keys().forEach { key ->
            val dimensionData = dimensions.getMap<String>(key) ?: return@forEach
            val generator = dimensionData.getMap<String>("generator") ?: return@forEach
            val type = generator.getString("type") ?: return@forEach
            when (type) {
                "minecraft:flat" -> {
                    val settings = generator.getMap<String>("settings") ?: return@forEach
                    settings.convert(MCTypeRegistry.BIOME, "biome", fromVersion, toVersion)
                    settings.getList("layers", ObjectType.MAP)?.let {
                        for (i in 0 until it.size()) {
                            it.getMap<String>(i).convert(MCTypeRegistry.BLOCK_NAME, "block", fromVersion, toVersion)
                        }
                    }
                }
                "minecraft:noise" -> {
                    generator.getMap<String>("settings")?.let {
                        it.convert(MCTypeRegistry.BLOCK_NAME, "default_block", fromVersion, toVersion)
                        it.convert(MCTypeRegistry.BLOCK_NAME, "default_fluid", fromVersion, toVersion)
                    }

                    generator.getMap<String>("biome_source")?.let { biomeSource ->
                        when (biomeSource.getString("type", "")!!) {
                            "minecraft:fixed" -> {
                                biomeSource.convert(MCTypeRegistry.BIOME, "biome", fromVersion, toVersion)
                            }
                            "minecraft:multi_noise" -> biomeSource.getList("biomes", ObjectType.MAP)?.let {
                                for (i in 0 until it.size()) {
                                    it.getMap<String>(i).convert(MCTypeRegistry.BIOME, "biome", fromVersion, toVersion)
                                }
                            }
                            "minecraft:checkerboard" -> {
                                biomeSource.convertList(MCTypeRegistry.BIOME, "biomes", fromVersion, toVersion)
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
        null
    }
}
