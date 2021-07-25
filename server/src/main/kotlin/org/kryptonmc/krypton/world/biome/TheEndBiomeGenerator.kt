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
package org.kryptonmc.krypton.world.biome

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.registry.RegistryLookupCodec
import org.kryptonmc.krypton.util.noise.SimplexNoise
import org.kryptonmc.krypton.util.random.WorldGenRandom

class TheEndBiomeGenerator private constructor(
    private val biomes: Registry<KryptonBiome>,
    private val seed: Long,
    private val end: KryptonBiome,
    private val highlands: KryptonBiome,
    private val midlands: KryptonBiome,
    private val islands: KryptonBiome,
    private val barrens: KryptonBiome
) : BiomeGenerator(listOf(end, highlands, midlands, islands, barrens)) {

    private val islandNoise = kotlin.run {
        val random = WorldGenRandom(seed).apply { skip(17292) }
        SimplexNoise(random)
    }
    override val codec = CODEC

    constructor(biomes: Registry<KryptonBiome>, seed: Long) : this(
        biomes,
        seed,
        biomes[BiomeKeys.THE_END]!!,
        biomes[BiomeKeys.END_HIGHLANDS]!!,
        biomes[BiomeKeys.END_MIDLANDS]!!,
        biomes[BiomeKeys.SMALL_END_ISLANDS]!!,
        biomes[BiomeKeys.END_BARRENS]!!
    )

    companion object {

        val CODEC: Codec<TheEndBiomeGenerator> = RecordCodecBuilder.create {
            it.group(
                RegistryLookupCodec(InternalResourceKeys.BIOME).forGetter(TheEndBiomeGenerator::biomes),
                Codec.LONG.fieldOf("seed").stable().forGetter(TheEndBiomeGenerator::seed)
            ).apply(it, ::TheEndBiomeGenerator)
        }
    }
}
