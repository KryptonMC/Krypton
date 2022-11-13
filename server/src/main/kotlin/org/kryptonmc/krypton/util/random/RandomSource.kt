/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.util.random

import java.util.concurrent.ThreadLocalRandom

/**
 * A source of randomness. This decouples the random generation from the source of randomness (implementation), and allows more freedom
 * than would be possible using built-in tools such as [java.util.Random].
 */
interface RandomSource {

    fun setSeed(seed: Long)

    fun nextInt(): Int

    fun nextInt(bound: Int): Int

    fun nextInt(min: Int, max: Int): Int {
        require(min < max) { "max - min is not positive!" }
        return min + nextInt(max - min)
    }

    fun nextLong(): Long

    fun nextBoolean(): Boolean

    /**
     * Returns a random float between 0.0F and 1.0F.
     */
    fun nextFloat(): Float

    /**
     * Returns a random double between 0.0 and 1.0.
     */
    fun nextDouble(): Double

    /**
     * Returns a random gaussian value with mean 0.0 and standard deviation 1.0.
     */
    fun nextGaussian(): Double

    /**
     * Returns a random value between a + b and a - b.
     */
    fun triangle(a: Double, b: Double): Double = a + b * (nextDouble() - nextDouble())

    companion object {

        /**
         * Same as create(seed).
         */
        @JvmStatic
        fun create(): RandomSource = create(RandomSupport.generateUniqueSeed())

        /**
         * The random source returned by this method is thread-safe. All operations are guaranteed to uphold their contracts in
         * a concurrent environment.
         */
        @JvmStatic
        fun createThreadSafe(): RandomSource = ThreadSafeLegacyRandomSource(RandomSupport.generateUniqueSeed())

        /**
         * The random source returned by this method is not thread-safe, and will ensure that certain operations are not called
         * concurrently, to ensure that the generator doesn't have undefined behaviour when accessed from a concurrent context.
         */
        @JvmStatic
        fun create(seed: Long): RandomSource = LegacyRandomSource(seed)

        /**
         * The random source returned by this method makes no guarantees about concurrent usage, and may break if used concurrently.
         */
        @JvmStatic
        fun createThreadLocal(): RandomSource = SingleThreadedRandomSource(ThreadLocalRandom.current().nextLong())
    }
}
