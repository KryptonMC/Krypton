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

import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.resource.InternalResourceKeys
import org.kryptonmc.krypton.world.biome.gen.MultiNoiseBiomeGenerator
import org.kryptonmc.krypton.world.biome.gen.TheEndBiomeGenerator
import org.kryptonmc.krypton.world.dimension.Dimension
import org.kryptonmc.krypton.world.dimension.KryptonDimensionTypes
import org.kryptonmc.krypton.world.generation.noise.NoiseGeneratorSettings
import java.util.Optional
import kotlin.random.Random

@JvmRecord
data class WorldGenerationSettings(
    val seed: Long,
    val generateFeatures: Boolean,
    val bonusChest: Boolean,
    val dimensions: Registry<Dimension>,
    val legacyCustomOptions: Optional<String>
) {

    constructor(
        seed: Long,
        generateFeatures: Boolean,
        bonusChest: Boolean,
        dimensions: Registry<Dimension>
    ) : this(seed, generateFeatures, bonusChest, dimensions, Optional.empty()) {
        checkNotNull(dimensions[Dimension.OVERWORLD]) { "Missing overworld settings!" }
    }

    val isDebug: Boolean
        get() = overworld() is DebugGenerator
    val isFlat: Boolean
        get() = overworld() is FlatGenerator

    fun overworld(): Generator = checkNotNull(dimensions[Dimension.OVERWORLD]) { "Missing overworld settings!" }.generator

    companion object {

        @JvmStatic
        fun default(): WorldGenerationSettings {
            val seed = Random.nextLong()
            return WorldGenerationSettings(
                seed,
                true,
                false,
                defaults(seed).withOverworld(defaultOverworld(seed))
            )
        }

        @JvmStatic
        private fun defaults(seed: Long): Registry<Dimension> = KryptonRegistry(InternalResourceKeys.DIMENSION).apply {
            register(Dimension.OVERWORLD, Dimension(KryptonDimensionTypes.OVERWORLD, defaultOverworld(seed)))
            register(Dimension.NETHER, Dimension(KryptonDimensionTypes.THE_NETHER, defaultNether(seed)))
            register(Dimension.END, Dimension(KryptonDimensionTypes.THE_END, defaultEnd(seed)))
        }

        @JvmStatic
        private fun defaultOverworld(seed: Long): NoiseGenerator = NoiseGenerator(
            MultiNoiseBiomeGenerator.Preset.OVERWORLD.createGenerator(Registries.BIOME),
            seed,
            InternalRegistries.NOISE_GENERATOR_SETTINGS[NoiseGeneratorSettings.OVERWORLD]!!
        )

        @JvmStatic
        private fun defaultNether(seed: Long): NoiseGenerator = NoiseGenerator(
            MultiNoiseBiomeGenerator.Preset.NETHER.createGenerator(Registries.BIOME),
            seed,
            InternalRegistries.NOISE_GENERATOR_SETTINGS[NoiseGeneratorSettings.NETHER]!!
        )

        @JvmStatic
        private fun defaultEnd(seed: Long): NoiseGenerator = NoiseGenerator(
            TheEndBiomeGenerator(seed),
            seed,
            InternalRegistries.NOISE_GENERATOR_SETTINGS[NoiseGeneratorSettings.END]!!
        )

        @JvmStatic
        private fun Registry<Dimension>.withOverworld(generator: Generator): KryptonRegistry<Dimension> {
            val overworld = get(Dimension.OVERWORLD)
            val overworldType = overworld?.type ?: KryptonDimensionTypes.OVERWORLD
            return withOverworld(overworldType, generator)
        }

        @JvmStatic
        private fun Registry<Dimension>.withOverworld(type: DimensionType, generator: Generator): KryptonRegistry<Dimension> {
            val registry = KryptonRegistry(InternalResourceKeys.DIMENSION).apply { register(Dimension.OVERWORLD, Dimension(type, generator)) }
            entries.forEach { (key, value) -> if (key !== Dimension.OVERWORLD) registry.register(key, value) }
            return registry
        }
    }
}
