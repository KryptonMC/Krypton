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
package org.kryptonmc.krypton.world.generation

import com.google.gson.JsonObject
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.Dynamic
import com.mojang.serialization.JsonOps
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.bardy.gsonkt.fromJson
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.GSON
import org.kryptonmc.krypton.config.KryptonConfig
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.RegistryDataPackCodec
import org.kryptonmc.krypton.registry.RegistryHolder
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.biome.VanillaLayeredBiomeGenerator
import org.kryptonmc.krypton.world.dimension.Dimension
import org.kryptonmc.krypton.world.dimension.Dimension.Companion.sort
import org.kryptonmc.krypton.world.dimension.Dimension.Companion.stable
import org.kryptonmc.krypton.world.dimension.DimensionTypes
import org.kryptonmc.krypton.world.dimension.defaults
import org.kryptonmc.krypton.world.generation.flat.FlatGeneratorSettings
import org.kryptonmc.krypton.world.generation.noise.NoiseGeneratorSettings
import java.util.Optional
import java.util.function.Function
import kotlin.random.Random

data class WorldGenerationSettings(
    val seed: Long,
    val generateFeatures: Boolean,
    val bonusChest: Boolean,
    val dimensions: KryptonRegistry<Dimension>,
    val legacyCustomOptions: Optional<String>
) {

    constructor(seed: Long, generateFeatures: Boolean, bonusChest: Boolean, dimensions: KryptonRegistry<Dimension>) : this(seed, generateFeatures, bonusChest, dimensions, Optional.empty()) {
        checkNotNull(dimensions[Dimension.OVERWORLD]) { "Missing overworld settings!" }
    }

    val isDebug: Boolean
        get() = overworld() is DebugGenerator
    val isFlat: Boolean
        get() = overworld() is FlatGenerator
    val isOldCustomized: Boolean
        get() = legacyCustomOptions.isPresent

    fun overworld() = checkNotNull(dimensions[Dimension.OVERWORLD]) { "Missing overworld settings!" }.generator

    private fun checkStable(): DataResult<WorldGenerationSettings> {
        dimensions[Dimension.OVERWORLD] ?: return DataResult.error("Missing overworld settings!")
        return if (stable()) DataResult.success(this, Lifecycle.stable()) else DataResult.success(this)
    }

    private fun stable() = dimensions.stable(seed)

    companion object {

        private val LOGGER = logger<WorldGenerationSettings>()
        val CODEC: Codec<WorldGenerationSettings> = RecordCodecBuilder.create<WorldGenerationSettings> { instance ->
            instance.group(
                Codec.LONG.fieldOf("seed").stable().forGetter(WorldGenerationSettings::seed),
                Codec.BOOL.fieldOf("generate_features").orElse(true).stable().forGetter(WorldGenerationSettings::generateFeatures),
                Codec.BOOL.fieldOf("bonus_chest").orElse(false).stable().forGetter(WorldGenerationSettings::bonusChest),
                RegistryDataPackCodec(InternalResourceKeys.DIMENSION, Dimension.CODEC).xmap({ it.sort() }, Function.identity()).fieldOf("dimensions").forGetter(WorldGenerationSettings::dimensions),
                Codec.STRING.optionalFieldOf("legacy_custom_options").stable().forGetter(WorldGenerationSettings::legacyCustomOptions)
            ).apply(instance, ::WorldGenerationSettings)
        }.comapFlatMap(WorldGenerationSettings::checkStable, Function.identity())

        fun makeDefault(dimensionTypes: Registry<DimensionType>, biomes: Registry<KryptonBiome>, noiseSettings: Registry<NoiseGeneratorSettings>): WorldGenerationSettings {
            val seed = Random.nextLong()
            return WorldGenerationSettings(seed, true, false, dimensionTypes.defaults(biomes, noiseSettings, seed).withOverworld(dimensionTypes, defaultOverworld(biomes, noiseSettings, seed)))
        }

        fun fromConfig(holder: RegistryHolder, config: KryptonConfig): WorldGenerationSettings {
            val generatorSettings = config.world.generator
            val settings = generatorSettings.settings
            val seed = generatorSettings.seed.takeIf { it.isNotBlank() }?.let { it.toLongOrNull() ?: it.hashCode().toLong() } ?: Random.nextLong()
            val type = generatorSettings.type
            val structures = generatorSettings.structures
            val dimensionTypes = holder.registryOrThrow(ResourceKeys.DIMENSION_TYPE)
            val biomes = holder.registryOrThrow(InternalResourceKeys.BIOME)
            val noiseSettings = holder.registryOrThrow(InternalResourceKeys.NOISE_GENERATOR_SETTINGS)
            val dimensions = dimensionTypes.defaults(biomes, noiseSettings, seed)
            return when (type) {
                "flat" -> {
                    val json = if (settings.isNotEmpty()) GSON.fromJson(settings) else JsonObject()
                    val dynamic = Dynamic(JsonOps.INSTANCE, json)
                    val parsed = FlatGeneratorSettings.CODEC.parse(dynamic)
                    return WorldGenerationSettings(seed, structures, false, dimensions.withOverworld(dimensionTypes, FlatGenerator(parsed.resultOrPartial(LOGGER::error).orElseGet { FlatGeneratorSettings.default(biomes) })))
                }
                "debug_all_block_states" -> WorldGenerationSettings(seed, structures, false, dimensions.withOverworld(dimensionTypes, DebugGenerator(biomes)))
                "amplified" -> WorldGenerationSettings(seed, structures, false, dimensions.withOverworld(dimensionTypes, NoiseGenerator(VanillaLayeredBiomeGenerator(seed, false, false, biomes), seed) { noiseSettings[NoiseGeneratorSettings.AMPLIFIED]!! }))
                "largebiomes" -> WorldGenerationSettings(seed, structures, false, dimensions.withOverworld(dimensionTypes, NoiseGenerator(VanillaLayeredBiomeGenerator(seed, false, true, biomes), seed) { noiseSettings[NoiseGeneratorSettings.OVERWORLD]!! }))
                else -> WorldGenerationSettings(seed, structures, false, dimensions.withOverworld(dimensionTypes, defaultOverworld(biomes, noiseSettings, seed)))
            }
        }

        fun defaultOverworld(biomes: Registry<KryptonBiome>, noiseSettings: Registry<NoiseGeneratorSettings>, seed: Long) = NoiseGenerator(
            VanillaLayeredBiomeGenerator(seed, false, false, biomes),
            seed
        ) { noiseSettings[NoiseGeneratorSettings.OVERWORLD]!! }
    }
}

private fun KryptonRegistry<Dimension>.withOverworld(dimensionTypes: Registry<DimensionType>, generator: Generator): KryptonRegistry<Dimension> {
    val overworld = get(Dimension.OVERWORLD)
    val overworldType = { overworld?.type ?: dimensionTypes[DimensionTypes.OVERWORLD_KEY]!! }
    return withOverworld(overworldType, generator)
}

private fun KryptonRegistry<Dimension>.withOverworld(typeSupplier: () -> DimensionType, generator: Generator): KryptonRegistry<Dimension> {
    val registry = KryptonRegistry(InternalResourceKeys.DIMENSION).apply { register(Dimension.OVERWORLD, Dimension(typeSupplier, generator)) }
    entries.forEach { (key, value) -> if (key !== Dimension.OVERWORLD) registry.register(key, value) }
    return registry
}
