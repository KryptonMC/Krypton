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

class SingleThreadedRandomSource(seed: Long) : BitRandomSource {

    private var seed = 0L
    private val gaussianSource = MarsagliaPolarGaussian(this)

    init {
        setSeed(seed)
    }

    override fun setSeed(seed: Long) {
        this.seed = seed xor MULTIPLIER and MODULUS_MASK
        gaussianSource.reset()
    }

    override fun next(bits: Int): Int {
        val nextSeed = seed * MULTIPLIER + INCREMENT and MODULUS_MASK
        seed = nextSeed
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
