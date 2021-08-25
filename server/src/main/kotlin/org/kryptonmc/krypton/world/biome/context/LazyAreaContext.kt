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
package org.kryptonmc.krypton.world.biome.context

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap
import org.kryptonmc.krypton.util.LinearCongruentialGenerator
import org.kryptonmc.krypton.util.noise.ImprovedNoise
import org.kryptonmc.krypton.util.random.SimpleRandomSource
import org.kryptonmc.krypton.world.biome.area.LazyArea
import org.kryptonmc.krypton.world.biome.layer.traits.PixelTransformer
import kotlin.math.max
import kotlin.math.min

class LazyAreaContext(private val maxCache: Int, seed: Long, salt: Long) : BigContext<LazyArea> {

    private val cache = Long2IntLinkedOpenHashMap(16, 0.25F).apply { defaultReturnValue(Int.MAX_VALUE) }
    private val seed = mixSeed(seed, salt)
    private var randomValue = 0L
    override val biomeNoise = ImprovedNoise(SimpleRandomSource(seed))

    override fun initRandom(seed: Long, salt: Long) {
        var random = this.seed
        random = LinearCongruentialGenerator.next(random, seed)
        random = LinearCongruentialGenerator.next(random, salt)
        random = LinearCongruentialGenerator.next(random, seed)
        random = LinearCongruentialGenerator.next(random, salt)
        randomValue = random
    }

    override fun nextRandom(bound: Int): Int {
        val floorMod = Math.floorMod(randomValue shr 24, bound)
        randomValue = LinearCongruentialGenerator.next(randomValue, seed)
        return floorMod
    }

    override fun createArea(transformer: PixelTransformer) = LazyArea(cache, maxCache, transformer)

    override fun createArea(transformer: PixelTransformer, parent: LazyArea) = LazyArea(cache, min(MAXIMUM_CACHE, parent.maxCache * 4), transformer)

    override fun createArea(transformer: PixelTransformer, firstParent: LazyArea, secondParent: LazyArea) = LazyArea(
        cache,
        min(MAXIMUM_CACHE, max(firstParent.maxCache, secondParent.maxCache) * 4),
        transformer
    )

    companion object {

        private const val MAXIMUM_CACHE = 1024

        private fun mixSeed(seed: Long, salt: Long): Long {
            var mixedSalt = LinearCongruentialGenerator.next(salt, salt)
            mixedSalt = LinearCongruentialGenerator.next(mixedSalt, salt)
            mixedSalt = LinearCongruentialGenerator.next(mixedSalt, salt)
            var mixedSeed = LinearCongruentialGenerator.next(seed, mixedSalt)
            mixedSeed = LinearCongruentialGenerator.next(mixedSeed, mixedSalt)
            return LinearCongruentialGenerator.next(mixedSeed, mixedSalt)
        }
    }
}
