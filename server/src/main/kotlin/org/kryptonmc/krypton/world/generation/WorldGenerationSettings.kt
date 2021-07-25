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

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.RegistryDataPackCodec
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.dimension.Dimension
import org.kryptonmc.krypton.world.dimension.Dimension.Companion.sort
import org.kryptonmc.krypton.world.dimension.Dimension.Companion.stable
import org.kryptonmc.krypton.world.dimension.DimensionTypes
import org.kryptonmc.krypton.world.dimension.defaults
import org.kryptonmc.krypton.world.generation.noise.NoiseGeneratorSettings
import java.util.Optional
import java.util.function.Function
import kotlin.random.Random

data class WorldGenerationSettings(
    val seed: Long,
    val generateFeatures: Boolean,
    val bonusChest: Boolean,
    val dimensions: KryptonRegistry<Dimension>,
    val legacyCustomOptions: Optional<String> = Optional.empty()
) {

    init {
        checkNotNull(dimensions[Dimension.OVERWORLD]) { "Missing overworld settings!" }
    }

    private fun checkStable(): DataResult<WorldGenerationSettings> {
        val overworld = dimensions[Dimension.OVERWORLD] ?: return DataResult.error("Missing overworld settings!")
        return if (stable()) DataResult.success(this, Lifecycle.stable()) else DataResult.success(this)
    }

    private fun stable() = dimensions.stable(seed)

    companion object {

        val CODEC: Codec<WorldGenerationSettings> = RecordCodecBuilder.create<WorldGenerationSettings> { instance ->
            instance.group(
                Codec.LONG.fieldOf("seed").stable().forGetter(WorldGenerationSettings::seed),
                Codec.BOOL.fieldOf("generate_features").orElse(true).stable().forGetter(WorldGenerationSettings::generateFeatures),
                Codec.BOOL.fieldOf("bonus_chest").orElse(false).stable().forGetter(WorldGenerationSettings::bonusChest),
                RegistryDataPackCodec(InternalResourceKeys.DIMENSION, Lifecycle.stable(), Dimension.CODEC).xmap({ it.sort() }, Function.identity()).fieldOf("dimensions").forGetter(WorldGenerationSettings::dimensions),
                Codec.STRING.optionalFieldOf("legacy_custom_options").stable().forGetter(WorldGenerationSettings::legacyCustomOptions)
            ).apply(instance, ::WorldGenerationSettings)
        }.comapFlatMap(WorldGenerationSettings::checkStable, Function.identity())

        fun makeDefault(dimensionTypes: Registry<DimensionType>, biomes: Registry<KryptonBiome>, noiseSettings: Registry<NoiseGeneratorSettings>): WorldGenerationSettings {
            val seed = Random.nextLong()
            // TODO: Use withOverworld to add the overworld generator
            return WorldGenerationSettings(seed, true, false, dimensionTypes.defaults(biomes, noiseSettings))
        }
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
