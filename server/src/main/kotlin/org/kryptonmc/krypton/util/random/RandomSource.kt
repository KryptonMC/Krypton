package org.kryptonmc.krypton.util.random

interface RandomSource {

    fun setSeed(seed: Long)

    fun nextInt(): Int

    fun nextInt(upper: Int): Int

    fun nextLong(): Long

    fun nextBoolean(): Boolean

    fun nextFloat(): Float

    fun nextDouble(): Double

    fun nextGaussian(): Double

    fun skip(count: Int) {
        for (i in 0 until count) nextInt()
    }
}
