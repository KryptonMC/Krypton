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

import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.doubles.DoubleArrayList
import it.unimi.dsi.fastutil.doubles.DoubleList
import net.kyori.adventure.key.Key
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.KryptonRegistry.Companion.directCodec
import org.kryptonmc.krypton.util.Codecs
import org.kryptonmc.krypton.util.successOrError
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.biome.Climate
import org.kryptonmc.krypton.world.biome.Climate.ParameterList
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.biome.KryptonBiomes
import org.kryptonmc.krypton.world.biome.OverworldBiomeBuilder
import java.util.function.Function
import java.util.function.Supplier

class MultiNoiseBiomeGenerator private constructor(
    private val parameters: ParameterList<Biome>,
    private val preset: Pair<KryptonRegistry<Biome>, Preset>? = null
) : BiomeGenerator(parameters.biomes.map { it.second.get() }) {

    private val presetInstance = if (preset != null) PresetInstance(preset.second, preset.first) else null
    override val codec: Codec<out BiomeGenerator> = CODEC

    override fun get(x: Int, y: Int, z: Int, sampler: Climate.Sampler): Biome = get(sampler.sample(x, y, z))

    private fun get(target: Climate.TargetPoint): Biome = parameters.findBiome(target) { KryptonBiomes.THE_VOID }

    private fun preset(): PresetInstance? = presetInstance

    @JvmRecord
    data class NoiseParameters(val firstOctave: Int, val amplitudes: DoubleList) {

        constructor(firstOctave: Int, amplitudes: List<Double>) : this(firstOctave, DoubleArrayList(amplitudes))

        companion object {

            @JvmField
            val CODEC: Codec<NoiseParameters> = RecordCodecBuilder.create {
                it.group(
                    Codec.INT.fieldOf("firstOctave").forGetter(NoiseParameters::firstOctave),
                    Codec.DOUBLE.listOf().fieldOf("amplitudes").forGetter(NoiseParameters::amplitudes)
                ).apply(it, MultiNoiseBiomeGenerator::NoiseParameters)
            }
        }
    }

    @JvmRecord
    data class Preset(val name: Key, val generator: Generator) {

        init {
            BY_KEY[name] = this
        }

        fun createGenerator(biomes: KryptonRegistry<Biome>): MultiNoiseBiomeGenerator = generator.create(this, biomes)

        fun interface Generator {

            fun create(preset: Preset, biomes: KryptonRegistry<Biome>): MultiNoiseBiomeGenerator
        }

        companion object {

            private val BY_KEY = mutableMapOf<Key, Preset>()

            @JvmField
            val NETHER: Preset = Preset(Key.key("nether")) { preset, biomes ->
                MultiNoiseBiomeGenerator(
                    ParameterList(ImmutableList.of(
                        Climate.ParameterPoint.ZERO to Supplier { biomes[BiomeKeys.NETHER_WASTES]!! },
                        Climate.parameters(0F, -0.5F, 0F, 0F, 0F, 0F, 0F) to
                                Supplier { biomes[BiomeKeys.SOUL_SAND_VALLEY]!! },
                        Climate.parameters(0.4F, 0F, 0F, 0F, 0F, 0F, 0F) to
                                Supplier { biomes[BiomeKeys.CRIMSON_FOREST]!! },
                        Climate.parameters(0F, 0.5F, 0F, 0F, 0F, 0F, 0.375F) to
                                Supplier { biomes[BiomeKeys.WARPED_FOREST]!! },
                        Climate.parameters(-0.5F, 0F, 0F, 0F, 0F, 0F, 0.175F) to
                                Supplier { biomes[BiomeKeys.BASALT_DELTAS]!! }
                    )),
                    Pair(biomes, preset)
                )
            }

            @JvmField
            val OVERWORLD: Preset = Preset(Key.key("overworld")) { preset, biomes ->
                val parameters = ImmutableList.builder<Pair<Climate.ParameterPoint, Supplier<Biome>>>()
                OverworldBiomeBuilder.addBiomes { point, key -> parameters.add(Pair(point, Supplier { biomes[key]!! })) }
                MultiNoiseBiomeGenerator(ParameterList(parameters.build()), Pair(biomes, preset))
            }

            @JvmStatic
            fun fromKey(key: Key): Preset? = BY_KEY[key]
        }
    }

    class PresetInstance(val preset: Preset, val biomes: KryptonRegistry<Biome>) {

        val generator: MultiNoiseBiomeGenerator = preset.createGenerator(biomes)

        companion object {

            @JvmField
            val CODEC: MapCodec<PresetInstance> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codecs.KEY.flatXmap(
                        { key -> Preset.fromKey(key).successOrError("Unknown generator preset $key!") },
                        { DataResult.success(it.name) }
                    ).fieldOf("preset")
                        .stable()
                        .forGetter(PresetInstance::preset),
                    ResourceKeys.BIOME.directCodec(KryptonBiome.CODEC).fieldOf("biomes").forGetter(PresetInstance::biomes)
                ).apply(instance, MultiNoiseBiomeGenerator::PresetInstance)
            }
        }
    }

    companion object {

        private val DIRECT_CODEC: MapCodec<MultiNoiseBiomeGenerator> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                RecordCodecBuilder.create<Pair<Climate.ParameterPoint, Supplier<Biome>>> { instance1 ->
                    instance1.group(
                        Climate.ParameterPoint.CODEC.fieldOf("parameters").forGetter { it.first },
                        KryptonBiome.CODEC.fieldOf("biome").forGetter { it.second.get() },
                    ).apply(instance1) { first, second -> Pair(first, Supplier { second }) }
                }.listOf()
                    .xmap(::ParameterList, ParameterList<Biome>::biomes)
                    .fieldOf("biomes")
                    .forGetter(MultiNoiseBiomeGenerator::parameters),
            ).apply(instance, ::MultiNoiseBiomeGenerator)
        }

        @JvmField
        val CODEC: Codec<MultiNoiseBiomeGenerator> = Codec.mapEither(PresetInstance.CODEC, DIRECT_CODEC).xmap(
            { either -> either.map(PresetInstance::generator, Function.identity()) },
            { generator -> if (generator.preset() != null) Either.left(generator.preset()) else Either.right(generator) }
        ).codec()
    }
}
