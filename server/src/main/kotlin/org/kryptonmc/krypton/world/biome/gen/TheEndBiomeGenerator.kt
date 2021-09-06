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

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.KryptonRegistry.Companion.directCodec
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.util.noise.SimplexNoise
import org.kryptonmc.krypton.util.random.WorldGenRandom
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.biome.KryptonBiome
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

class TheEndBiomeGenerator private constructor(
    private val biomes: KryptonRegistry<KryptonBiome>,
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

    constructor(biomes: KryptonRegistry<KryptonBiome>, seed: Long) : this(
        biomes,
        seed,
        biomes[BiomeKeys.THE_END]!!,
        biomes[BiomeKeys.END_HIGHLANDS]!!,
        biomes[BiomeKeys.END_MIDLANDS]!!,
        biomes[BiomeKeys.SMALL_END_ISLANDS]!!,
        biomes[BiomeKeys.END_BARRENS]!!
    )

    override fun get(x: Int, y: Int, z: Int): KryptonBiome {
        val quartX = x shr 2
        val quartZ = z shr 2
        if (quartX.toLong() * quartX.toLong() + quartZ.toLong() * quartZ.toLong() <= ISLAND_CHUNK_DISTANCE_SQ) return end
        val heightValue = islandNoise.getHeightValue(quartX * 2 + 1, quartZ * 2 + 1)
        return when {
            heightValue > 40F -> highlands
            heightValue >= 0F -> midlands
            heightValue < -20F -> islands
            else -> barrens
        }
    }

    companion object {

        val CODEC: Codec<TheEndBiomeGenerator> = RecordCodecBuilder.create {
            it.group(
                InternalResourceKeys.BIOME.directCodec(KryptonBiome.CODEC).fieldOf("biomes")
                    .forGetter(TheEndBiomeGenerator::biomes),
                Codec.LONG.fieldOf("seed").stable().forGetter(TheEndBiomeGenerator::seed)
            ).apply(it, ::TheEndBiomeGenerator)
        }
        private const val ISLAND_THRESHOLD = -0.9
        private const val ISLAND_CHUNK_DISTANCE_SQ = 4096L

        fun SimplexNoise.getHeightValue(x: Int, z: Int): Float {
            val divX = x / 2
            val divZ = z / 2
            val modX = x % 2
            val modZ = z % 2
            var value = (100F - sqrt((x * x + z * z).toFloat()) * 8F).clamp(-100F, 80F)
            for (xo in -12..12) {
                for (zo in -12..12) {
                    val offX = (divX + xo).toLong()
                    val offZ = (divZ + zo).toLong()
                    if (
                        offX * offX + offZ * offZ > ISLAND_CHUNK_DISTANCE_SQ &&
                        getValue(offX.toDouble(), offZ.toDouble()) < ISLAND_THRESHOLD
                    ) {
                        val abs = (abs(offX.toFloat()) * 3439F + abs(offZ.toFloat()) * 147F) % 13F + 9F
                        val offModX = (modX - xo * 2).toFloat()
                        val offModZ = (modZ - zo * 2).toFloat()
                        val newValue = (100F - sqrt(offModX * offModX + offModZ * offModZ) * abs).clamp(-100F, 80F)
                        value = max(value, newValue)
                    }
                }
            }
            return value
        }
    }
}
