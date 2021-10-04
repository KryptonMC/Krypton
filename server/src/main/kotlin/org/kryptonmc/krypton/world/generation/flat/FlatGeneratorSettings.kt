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
package org.kryptonmc.krypton.world.generation.flat

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.KryptonRegistry.Companion.directCodec
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.generation.StructureSettings
import java.util.Optional
import java.util.function.Function

class FlatGeneratorSettings(
    private val biomes: KryptonRegistry<Biome>,
    val structureSettings: StructureSettings
) {

    val layers = mutableListOf<FlatLayer>()
    private val blockLayers = mutableListOf<Block>()
    var biome = biomes[BiomeKeys.PLAINS]!!
        private set
    private var generateVoid = false
    var decorate = false
    var addLakes = false

    constructor(
        biomes: KryptonRegistry<Biome>,
        structureSettings: StructureSettings,
        layers: List<FlatLayer>,
        addLakes: Boolean,
        decorate: Boolean,
        biome: Optional<Biome>
    ) : this(biomes, structureSettings) {
        if (addLakes) this.addLakes = true
        if (decorate) this.decorate = true
        this.layers.addAll(layers)
        updateLayers()
        this.biome = if (!biome.isPresent) {
            biomes[BiomeKeys.PLAINS]!!
        } else biome.get()
    }

    private fun updateLayers() {
        blockLayers.clear()
        layers.forEach { for (i in 0 until it.height) blockLayers.add(it.block) }
        generateVoid = blockLayers.all { it === Blocks.AIR }
    }

    companion object {

        val CODEC: Codec<FlatGeneratorSettings> = RecordCodecBuilder.create<FlatGeneratorSettings> { instance ->
            instance.group(
                ResourceKeys.BIOME.directCodec(KryptonBiome.CODEC)
                    .fieldOf("biomes")
                    .forGetter(FlatGeneratorSettings::biomes),
                StructureSettings.CODEC.fieldOf("structures").forGetter(FlatGeneratorSettings::structureSettings),
                FlatLayer.CODEC.listOf().fieldOf("layers").forGetter(FlatGeneratorSettings::layers),
                Codec.BOOL.fieldOf("lakes").orElse(false).forGetter(FlatGeneratorSettings::addLakes),
                Codec.BOOL.fieldOf("features").orElse(false).forGetter(FlatGeneratorSettings::decorate),
                KryptonBiome.CODEC.optionalFieldOf("biome")
                    .orElseGet { Optional.empty() }
                    .forGetter { Optional.of(it.biome) },
            ).apply(instance, ::FlatGeneratorSettings)
        }.comapFlatMap(::validateHeight, Function.identity()).stable()

        fun default(biomes: KryptonRegistry<Biome>): FlatGeneratorSettings {
            // TODO: Add village structure to the map
            val structureSettings = StructureSettings(mapOf(), StructureSettings.DEFAULT_STRONGHOLD)
            return FlatGeneratorSettings(biomes, structureSettings).apply {
                this.biome = biomes[BiomeKeys.PLAINS]!!
                layers.add(FlatLayer(Blocks.BEDROCK, 1))
                layers.add(FlatLayer(Blocks.DIRT, 2))
                layers.add(FlatLayer(Blocks.GRASS_BLOCK, 1))
                updateLayers()
            }
        }

        private fun validateHeight(settings: FlatGeneratorSettings): DataResult<FlatGeneratorSettings> {
            val heightSum = settings.layers.sumOf { it.height }
            return if (heightSum > KryptonDimensionType.Y_SIZE) {
                DataResult.error("Sum of layer heights is greater than the maximum sum of heights " +
                        "${KryptonDimensionType.Y_SIZE}!", settings)
            } else {
                DataResult.success(settings)
            }
        }
    }
}
