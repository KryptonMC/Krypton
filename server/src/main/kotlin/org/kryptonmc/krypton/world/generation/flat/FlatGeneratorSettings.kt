/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.world.generation.flat

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.generation.StructureSettings
import java.util.Optional

class FlatGeneratorSettings(val structureSettings: StructureSettings) {

    val layers: MutableList<FlatLayer> = mutableListOf()
    private val blockLayers = mutableListOf<Block>()
    var biome: Biome = Registries.BIOME[BiomeKeys.PLAINS]!!
        private set
    private var generateVoid = false
    private var decorate = false
    private var addLakes = false

    constructor(
        structureSettings: StructureSettings,
        layers: List<FlatLayer>,
        addLakes: Boolean,
        decorate: Boolean,
        biome: Optional<Biome>
    ) : this(structureSettings) {
        if (addLakes) this.addLakes = true
        if (decorate) this.decorate = true
        this.layers.addAll(layers)
        updateLayers()
        this.biome = biome.orElse(Registries.BIOME[BiomeKeys.PLAINS])
    }

    private fun updateLayers() {
        blockLayers.clear()
        layers.forEach { for (i in 0 until it.height) blockLayers.add(it.block) }
        generateVoid = blockLayers.all { it === Blocks.AIR }
    }

    companion object {

        @JvmStatic
        fun default(): FlatGeneratorSettings {
            // TODO: Add village structure to the map
            val structureSettings = StructureSettings(mapOf(), StructureSettings.DEFAULT_STRONGHOLD)
            return FlatGeneratorSettings(structureSettings).apply {
                this.biome = Registries.BIOME[BiomeKeys.PLAINS]!!
                layers.add(FlatLayer(Blocks.BEDROCK, 1))
                layers.add(FlatLayer(Blocks.DIRT, 2))
                layers.add(FlatLayer(Blocks.GRASS_BLOCK, 1))
                updateLayers()
            }
        }
    }
}
