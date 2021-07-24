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
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.registry.RegistryLookupCodec
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.dimension.DimensionTypes
import org.kryptonmc.krypton.world.generation.StructureSettings
import java.util.Optional
import java.util.function.Function
import java.util.function.Supplier

class FlatGeneratorSettings(
    private val biomes: Registry<KryptonBiome>,
    val structureSettings: StructureSettings
) {

    val layers = mutableListOf<FlatLayer>()
    val blockLayers = mutableListOf<Block>()
    private var biomeSupplier = Supplier { biomes[BiomeKeys.PLAINS]!! }
    private var generateVoid = false
    var decorate = false
    var addLakes = false

    constructor(
        biomes: Registry<KryptonBiome>,
        structureSettings: StructureSettings,
        layers: List<FlatLayer>,
        addLakes: Boolean,
        decorate: Boolean,
        biome: Optional<Supplier<KryptonBiome>>
    ) : this(biomes, structureSettings) {
        if (addLakes) this.addLakes = true
        if (decorate) this.decorate = true
        this.layers.addAll(layers)
        updateLayers()
        this.biomeSupplier = if (!biome.isPresent) {
            LOGGER.warn("Unknown biome, defaulting to plains...")
            Supplier { biomes[BiomeKeys.PLAINS]!! }
        } else biome.get()
    }

    private fun updateLayers() {
        blockLayers.clear()
        layers.forEach { for (i in 0 until it.height) blockLayers.add(it.block) }
        generateVoid = blockLayers.all { it === Blocks.AIR }
    }

    val biome: KryptonBiome
        get() = biomeSupplier.get()

    companion object {

        private val LOGGER = logger<FlatGeneratorSettings>()
        val CODEC: Codec<FlatGeneratorSettings> = RecordCodecBuilder.create<FlatGeneratorSettings> { instance ->
            instance.group(
                RegistryLookupCodec(InternalResourceKeys.BIOME).forGetter(FlatGeneratorSettings::biomes),
                StructureSettings.CODEC.fieldOf("structures").forGetter(FlatGeneratorSettings::structureSettings),
                FlatLayer.CODEC.listOf().fieldOf("layers").forGetter(FlatGeneratorSettings::layers),
                Codec.BOOL.fieldOf("lakes").orElse(false).forGetter(FlatGeneratorSettings::addLakes),
                Codec.BOOL.fieldOf("features").orElse(false).forGetter(FlatGeneratorSettings::decorate),
                KryptonBiome.CODEC.optionalFieldOf("biome").orElseGet { Optional.empty() }.forGetter { Optional.of(it.biomeSupplier) },
            ).apply(instance, ::FlatGeneratorSettings)
        }.comapFlatMap(FlatGeneratorSettings::validateHeight, Function.identity()).stable()
    }
}

private fun FlatGeneratorSettings.validateHeight(): DataResult<FlatGeneratorSettings> {
    val heightSum = layers.sumOf { it.height }
    return if (heightSum > DimensionTypes.Y_SIZE) DataResult.error("Sum of layer heights is greater than the maximum sum of heights ${DimensionTypes.Y_SIZE}!", this) else DataResult.success(this)
}
