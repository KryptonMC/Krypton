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

import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.util.noise.SimplexNoise
import org.kryptonmc.krypton.util.random.WorldGenRandom
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.biome.Climate
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

class TheEndBiomeGenerator private constructor(
    private val seed: Long,
    private val end: Biome,
    private val highlands: Biome,
    private val midlands: Biome,
    private val islands: Biome,
    private val barrens: Biome
) : BiomeGenerator(listOf(end, highlands, midlands, islands, barrens)) {

    private val islandNoise = kotlin.run {
        val random = WorldGenRandom(seed).apply { skip(17292) }
        SimplexNoise(random)
    }

    constructor(seed: Long) : this(
        seed,
        Registries.BIOME[BiomeKeys.THE_END]!!,
        Registries.BIOME[BiomeKeys.END_HIGHLANDS]!!,
        Registries.BIOME[BiomeKeys.END_MIDLANDS]!!,
        Registries.BIOME[BiomeKeys.SMALL_END_ISLANDS]!!,
        Registries.BIOME[BiomeKeys.END_BARRENS]!!
    )

    override fun get(x: Int, y: Int, z: Int, sampler: Climate.Sampler): Biome {
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

        private const val ISLAND_THRESHOLD = -0.9
        private const val ISLAND_CHUNK_DISTANCE_SQ = 4096L

        @JvmStatic
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
                    if (offX * offX + offZ * offZ > ISLAND_CHUNK_DISTANCE_SQ && getValue(offX.toDouble(), offZ.toDouble()) < ISLAND_THRESHOLD) {
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
