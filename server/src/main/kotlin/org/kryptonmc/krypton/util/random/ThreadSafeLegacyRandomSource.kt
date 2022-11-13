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

import java.util.concurrent.atomic.AtomicLong

class ThreadSafeLegacyRandomSource(seed: Long) : BitRandomSource {

    private val seed = AtomicLong()
    private val gaussianSource = MarsagliaPolarGaussian(this)

    init {
        setSeed(seed)
    }

    override fun setSeed(seed: Long) {
        this.seed.set(seed xor MULTIPLIER and MODULUS_MASK)
    }

    override fun next(bits: Int): Int {
        var nextSeed: Long
        do {
            val seed = this.seed.get()
            nextSeed = seed * MULTIPLIER + INCREMENT and MODULUS_MASK
        } while (!this.seed.compareAndSet(seed, nextSeed))
        return (nextSeed shr MODULUS_BITS - bits).toInt()
    }

    override fun nextGaussian(): Double = gaussianSource.nextGaussian()

    companion object {

        private const val MODULUS_BITS = 48
        private const val MODULUS_MASK = 281474976710655L
        private const val MULTIPLIER = 25214903917L
        private const val INCREMENT = 11L
    }
}
