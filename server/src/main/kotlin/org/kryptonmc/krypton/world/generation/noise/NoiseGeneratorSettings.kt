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
package org.kryptonmc.krypton.world.generation.noise

import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.resource.InternalResourceKeys
import org.kryptonmc.krypton.world.generation.StructureSettings

@JvmRecord
data class NoiseGeneratorSettings(
    val structureSettings: StructureSettings,
    val noiseSettings: NoiseSettings,
    val defaultBlock: Block,
    val defaultFluid: Block,
    val bedrockRoofPosition: Int,
    val bedrockFloorPosition: Int,
    val seaLevel: Int,
    val minimumSurfaceLevel: Int,
    val disableMobGeneration: Boolean,
    val aquifiersEnabled: Boolean,
    val noiseCavesEnabled: Boolean,
    val deepslateEnabled: Boolean,
    val oreVeinsEnabled: Boolean,
    val noodleCavesEnabled: Boolean
) {

    companion object {

        @JvmField
        val OVERWORLD: ResourceKey<NoiseGeneratorSettings> = ResourceKey.of(InternalResourceKeys.NOISE_GENERATOR_SETTINGS, key("overworld"))
        @JvmField
        val NETHER: ResourceKey<NoiseGeneratorSettings> = ResourceKey.of(InternalResourceKeys.NOISE_GENERATOR_SETTINGS, key("nether"))
        @JvmField
        val END: ResourceKey<NoiseGeneratorSettings> = ResourceKey.of(InternalResourceKeys.NOISE_GENERATOR_SETTINGS, key("end"))

        private val AMPLIFIED = ResourceKey.of(InternalResourceKeys.NOISE_GENERATOR_SETTINGS, key("amplified"))
        private val CAVES = ResourceKey.of(InternalResourceKeys.NOISE_GENERATOR_SETTINGS, key("caves"))
        private val FLOATING_ISLANDS = ResourceKey.of(InternalResourceKeys.NOISE_GENERATOR_SETTINGS, key("floating_islands"))

        init {
            register(OVERWORLD, overworld(StructureSettings(true), false))
            register(AMPLIFIED, overworld(StructureSettings(true), true))
            register(NETHER, netherLike(StructureSettings(false), Blocks.NETHERRACK, Blocks.LAVA))
            register(END, endLike(
                StructureSettings(false),
                Blocks.END_STONE,
                Blocks.AIR,
                true,
                true
            ))
            register(CAVES, netherLike(StructureSettings(true), Blocks.STONE, Blocks.WATER))
            register(FLOATING_ISLANDS, endLike(
                StructureSettings(true),
                Blocks.STONE,
                Blocks.WATER,
                false,
                false
            ))
        }

        @JvmStatic
        private fun register(
            key: ResourceKey<NoiseGeneratorSettings>,
            settings: NoiseGeneratorSettings
        ): NoiseGeneratorSettings = settings.apply { InternalRegistries.NOISE_GENERATOR_SETTINGS.register(key.location, this) }

        @JvmStatic
        private fun overworld(structureSettings: StructureSettings, isAmplified: Boolean): NoiseGeneratorSettings {
            val scale = 0.9999999814507745
            return NoiseGeneratorSettings(
                structureSettings,
                NoiseSettings(
                    0,
                    256,
                    NoiseSampling(scale, scale, 80.0, 160.0),
                    NoiseSlide(-10, 3, 0),
                    NoiseSlide(15, 3, 0),
                    1,
                    2,
                    1.0,
                    -0.46875,
                    true,
                    true,
                    false,
                    isAmplified
                ),
                Blocks.STONE,
                Blocks.WATER,
                Int.MIN_VALUE,
                0,
                63,
                0,
                false,
                false,
                false,
                false,
                false,
                false
            )
        }

        @JvmStatic
        private fun netherLike(
            structureSettings: StructureSettings,
            defaultBlock: Block,
            defaultFluid: Block
        ): NoiseGeneratorSettings {
            val structures = StructureSettings.DEFAULTS.toMutableMap() // TODO: Add ruined portal to this map when it exists
            return NoiseGeneratorSettings(
                StructureSettings(
                    structures,
                    structureSettings.stronghold
                ),
                NoiseSettings(
                    0,
                    128,
                    NoiseSampling(1.0, 3.0, 80.0, 60.0),
                    NoiseSlide(120, 3, 0),
                    NoiseSlide(320, 4, -1),
                    1,
                    2,
                    0.0,
                    0.019921875,
                    false,
                    false,
                    false,
                    false
                ),
                defaultBlock,
                defaultFluid,
                0,
                0,
                32,
                0,
                false,
                false,
                false,
                false,
                false,
                false
            )
        }

        @JvmStatic
        private fun endLike(
            structureSettings: StructureSettings,
            defaultBlock: Block,
            defaultFluid: Block,
            disableMobGeneration: Boolean,
            islandNoiseOverride: Boolean
        ): NoiseGeneratorSettings = NoiseGeneratorSettings(
            structureSettings,
            NoiseSettings(
                0,
                128,
                NoiseSampling(2.0, 1.0, 80.0, 160.0),
                NoiseSlide(-3000, 64, -46),
                NoiseSlide(-30, 7, 1),
                2,
                1,
                0.0,
                0.0,
                true,
                false,
                islandNoiseOverride,
                false
            ),
            defaultBlock,
            defaultFluid,
            Int.MIN_VALUE,
            Int.MIN_VALUE,
            0,
            0,
            disableMobGeneration,
            false,
            false,
            false,
            false,
            false
        )
    }
}
