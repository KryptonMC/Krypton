/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
