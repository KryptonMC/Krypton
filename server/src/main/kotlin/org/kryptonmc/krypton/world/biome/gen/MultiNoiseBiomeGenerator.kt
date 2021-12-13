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
import it.unimi.dsi.fastutil.doubles.DoubleArrayList
import it.unimi.dsi.fastutil.doubles.DoubleList
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.biome.Climate
import org.kryptonmc.krypton.world.biome.Climate.ParameterList
import org.kryptonmc.krypton.world.biome.KryptonBiomes
import org.kryptonmc.krypton.world.biome.OverworldBiomeBuilder
import java.util.function.Supplier

class MultiNoiseBiomeGenerator private constructor(
    private val parameters: ParameterList<Biome>,
    private val preset: Pair<Registry<Biome>, Preset>? = null
) : BiomeGenerator(parameters.biomes.map { it.second.get() }) {

    private val presetInstance: PresetInstance? by lazy { if (preset != null) PresetInstance(preset.second, preset.first) else null }

    override fun get(x: Int, y: Int, z: Int, sampler: Climate.Sampler): Biome = get(sampler.sample(x, y, z))

    private fun get(target: Climate.TargetPoint): Biome = parameters.findBiome(target) { KryptonBiomes.THE_VOID }

    private fun preset(): PresetInstance? = presetInstance

    @JvmRecord
    data class NoiseParameters(val firstOctave: Int, val amplitudes: DoubleList) {

        constructor(firstOctave: Int, amplitudes: List<Double>) : this(firstOctave, DoubleArrayList(amplitudes))
    }

    @JvmRecord
    data class Preset(val name: Key, val generator: Generator) {

        init {
            BY_KEY[name] = this
        }

        fun createGenerator(biomes: Registry<Biome>): MultiNoiseBiomeGenerator = generator.create(this, biomes)

        fun interface Generator {

            fun create(preset: Preset, biomes: Registry<Biome>): MultiNoiseBiomeGenerator
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

    class PresetInstance(val preset: Preset, val biomes: Registry<Biome>) {

        val generator: MultiNoiseBiomeGenerator = preset.createGenerator(biomes)
    }
}
