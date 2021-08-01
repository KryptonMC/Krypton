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
package org.kryptonmc.krypton.world.biome.gen

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.doubles.DoubleArrayList
import it.unimi.dsi.fastutil.doubles.DoubleList
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.registry.RegistryLookupCodec
import org.kryptonmc.krypton.util.KEY_CODEC
import org.kryptonmc.krypton.util.noise.NormalNoise
import org.kryptonmc.krypton.util.random.WorldGenRandom
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.biome.ClimateParameters
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.biome.KryptonBiomes
import java.util.function.Function

class MultiNoiseBiomeGenerator private constructor(
    private val seed: Long,
    private val parameters: List<Pair<ClimateParameters, () -> KryptonBiome>>,
    private val temperatureParameters: NoiseParameters = DEFAULT_PARAMETERS,
    private val humidityParameters: NoiseParameters = DEFAULT_PARAMETERS,
    private val altitudeParameters: NoiseParameters = DEFAULT_PARAMETERS,
    private val weirdnessParameters: NoiseParameters = DEFAULT_PARAMETERS,
    private val preset: Pair<Registry<KryptonBiome>, Preset>? = null
) : BiomeGenerator(parameters.asSequence().map { it.second }) {

    private val temperatureNoise = NormalNoise(WorldGenRandom(seed), temperatureParameters.firstOctave, temperatureParameters.amplitudes)
    private val humidityNoise = NormalNoise(WorldGenRandom(seed + 1L), humidityParameters.firstOctave, humidityParameters.amplitudes)
    private val altitudeNoise = NormalNoise(WorldGenRandom(seed + 2L), altitudeParameters.firstOctave, altitudeParameters.amplitudes)
    private val weirdnessNoise = NormalNoise(WorldGenRandom(seed + 3L), weirdnessParameters.firstOctave, weirdnessParameters.amplitudes)
    private val useY = false

    override val codec = CODEC

    override fun get(x: Int, y: Int, z: Int): KryptonBiome {
        val usedY = (if (useY) y else 0).toDouble()
        val climateParameters = ClimateParameters(
            temperatureNoise.getValue(x.toDouble(), usedY, z.toDouble()).toFloat(),
            humidityNoise.getValue(x.toDouble(), usedY, z.toDouble()).toFloat(),
            altitudeNoise.getValue(x.toDouble(), usedY, z.toDouble()).toFloat(),
            weirdnessNoise.getValue(x.toDouble(), usedY, z.toDouble()).toFloat(),
            0F
        )
        return parameters.minByOrNull { it.first.fitness(climateParameters) }?.second?.invoke() ?: KryptonBiomes.THE_VOID
    }

    private fun preset() = preset?.let { PresetInstance(it.second, it.first, seed) }

    class NoiseParameters(val firstOctave: Int, val amplitudes: DoubleList) {

        constructor(firstOctave: Int, amplitudes: List<Double>) : this(firstOctave, DoubleArrayList(amplitudes))

        constructor(firstOctave: Int, vararg amplitudes: Double) : this(firstOctave, DoubleArrayList(amplitudes))

        companion object {

            val CODEC: Codec<NoiseParameters> = RecordCodecBuilder.create {
                it.group(
                    Codec.INT.fieldOf("firstOctave").forGetter(NoiseParameters::firstOctave),
                    Codec.DOUBLE.listOf().fieldOf("amplitudes").forGetter(NoiseParameters::amplitudes)
                ).apply(it, MultiNoiseBiomeGenerator::NoiseParameters)
            }
        }
    }

    class Preset(
        val name: Key,
        val generator: (Preset, Registry<KryptonBiome>, Long) -> MultiNoiseBiomeGenerator
    ) {

        init {
            BY_NAME[name] = this
        }

        fun generator(biomes: Registry<KryptonBiome>, seed: Long) = generator(this, biomes, seed)

        companion object {

            val BY_NAME = mutableMapOf<Key, Preset>()
            val NETHER = Preset(Key.key("nether")) { preset, biomes, seed -> MultiNoiseBiomeGenerator(seed, listOf(
                ClimateParameters(0F, 0F, 0F, 0F, 0F) to { biomes[BiomeKeys.NETHER_WASTES]!!},
                ClimateParameters(0F, -0.5F, 0F, 0F, 0F) to { biomes[BiomeKeys.SOUL_SAND_VALLEY]!! },
                ClimateParameters(0.4F, 0F, 0F, 0F, 0F) to { biomes[BiomeKeys.CRIMSON_FOREST]!! },
                ClimateParameters(0F, 0.5F, 0F, 0F, 0.375F) to { biomes[BiomeKeys.WARPED_FOREST]!! },
                ClimateParameters(-0.5F, 0F, 0F, 0F, 0.175F) to { biomes[BiomeKeys.BASALT_DELTAS]!! }
            ), preset = biomes to preset) }
        }
    }

    class PresetInstance(val preset: Preset, val biomes: Registry<KryptonBiome>, val seed: Long) {

        val generator = preset.generator(biomes, seed)

        companion object {

            val CODEC: MapCodec<PresetInstance> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    KEY_CODEC.flatXmap(
                        { key -> Preset.BY_NAME[key]?.let { DataResult.success(it) } ?: DataResult.error("Unknown generator preset $key!") },
                        { DataResult.success(it.name) }
                    ).fieldOf("preset").stable().forGetter(PresetInstance::preset),
                    RegistryLookupCodec(InternalResourceKeys.BIOME).forGetter(PresetInstance::biomes),
                    Codec.LONG.fieldOf("seed").stable().forGetter(PresetInstance::seed)
                ).apply(instance, MultiNoiseBiomeGenerator::PresetInstance)
            }
        }
    }

    companion object {

        private val DEFAULT_PARAMETERS = NoiseParameters(-7, listOf(1.0, 1.0))
        val DIRECT_CODEC: MapCodec<MultiNoiseBiomeGenerator> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.LONG.fieldOf("seed").forGetter(MultiNoiseBiomeGenerator::seed),
                RecordCodecBuilder.create<Pair<ClimateParameters, () -> KryptonBiome>> { instance1 ->
                    instance1.group(
                        ClimateParameters.CODEC.fieldOf("parameters").forGetter { it.first },
                        KryptonBiome.CODEC.fieldOf("biome").forGetter { { it.second() } },
                    ).apply(instance1) { first, second -> Pair(first) { second() } }
                }.listOf().fieldOf("biomes").forGetter(MultiNoiseBiomeGenerator::parameters),
                NoiseParameters.CODEC.fieldOf("temperature_noise").forGetter(MultiNoiseBiomeGenerator::temperatureParameters),
                NoiseParameters.CODEC.fieldOf("humidity_noise").forGetter(MultiNoiseBiomeGenerator::humidityParameters),
                NoiseParameters.CODEC.fieldOf("altitude_noise").forGetter(MultiNoiseBiomeGenerator::altitudeParameters),
                NoiseParameters.CODEC.fieldOf("weirdness_noise").forGetter(MultiNoiseBiomeGenerator::weirdnessParameters)
            ).apply(instance, ::MultiNoiseBiomeGenerator)
        }
        val CODEC: Codec<MultiNoiseBiomeGenerator> = Codec.mapEither(PresetInstance.CODEC, DIRECT_CODEC).xmap(
            { either -> either.map(PresetInstance::generator, Function.identity()) },
            { generator -> generator.preset()?.let { Either.left(it) } ?: Either.right(generator) }
        ).codec()
    }
}
