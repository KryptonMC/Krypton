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
package org.kryptonmc.krypton.util

import com.google.common.math.IntMath
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.spongepowered.math.GenericMath
import org.spongepowered.math.vector.Vector3i
import java.util.UUID
import java.util.function.IntPredicate
import kotlin.math.sqrt
import kotlin.random.Random

object Maths {

    private val MULTIPLY_DE_BRUIJN_BIT_POSITION = intArrayOf(
        0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8,
        31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
    )
    const val EPSILON: Float = 1.0E-5F

    /**
     * Calculates a chunk position from a given [id] in a spiral pattern.
     *
     * **Algorithm:**
     *
     * Given n, an index in the squared spiral
     * p, the sum of a point in the inner square
     * and a, the position on the current square
     *
     * n = p + a
     *
     * Credit for this algorithm goes to
     * [davidonet](https://stackoverflow.com/users/1068670/davidonet) (for the original algorithm),
     * and [Esophose](https://github.com/Esophose) (for the Kotlin conversion and modifications)
     *
     * See [here](https://stackoverflow.com/questions/398299/looping-in-a-spiral) for original
     *
     * @param id the id in the spiral
     * @param xOffset an optional X offset
     * @param zOffset an optional Z offset
     * @return a [ChunkPosition] containing the calculated position in the spiral.
     */
    @JvmStatic
    fun chunkInSpiral(id: Int, xOffset: Int = 0, zOffset: Int = 0): Long {
        // if the id is 0 then we know we're in the centre
        if (id == 0) return ChunkPosition.toLong(0 + xOffset, 0 + zOffset)

        val index = id - 1

        // compute radius (inverse arithmetic sum of 8 + 16 + 24 + ...)
        val radius = floor((sqrt(index + 1.0) - 1) / 2) + 1

        // compute total point on radius -1 (arithmetic sum of 8 + 16 + 24 + ...)
        val p = 8 * radius * (radius - 1) / 2

        // points by face
        val en = radius * 2

        // compute de position and shift it so the first is (-r, -r) but (-r + 1, -r)
        // so the square can connect
        val a = (1 + index - p) % (radius * 8)

        return when (a / (radius * 2)) {
            // find the face (0 = top, 1 = right, 2 = bottom, 3 = left)
            0 -> ChunkPosition.toLong(a - radius + xOffset, -radius + zOffset)
            1 -> ChunkPosition.toLong(radius + xOffset, a % en - radius + zOffset)
            2 -> ChunkPosition.toLong(radius - a % en + xOffset, radius + zOffset)
            3 -> ChunkPosition.toLong(-radius + xOffset, radius - a % en + zOffset)
            else -> ChunkPosition.ZERO.toLong()
        }
    }

    @JvmStatic
    fun lcm(a: Int, b: Int): Long = a.toLong() * (b / IntMath.gcd(a, b))

    @JvmStatic
    fun fastBinarySearch(minimum: Int, maximum: Int, predicate: IntPredicate): Int {
        var range = maximum - minimum
        var tempMinimum = minimum
        while (range > 0) {
            val halfRange = range / 2
            val halfOffset = tempMinimum + halfRange
            if (predicate.test(halfOffset)) {
                range = halfRange
            } else {
                tempMinimum = halfOffset + 1
                range -= halfRange + 1
            }
        }
        return tempMinimum
    }

    // The magic values in here are from the mixer parts of MurmurHash3
    @JvmStatic
    @Suppress("MagicNumber")
    fun murmurHash3Mixer(value: Int): Int {
        var temp = value
        temp = temp xor (temp ushr 16)
        temp *= -2048144789
        temp = temp xor (temp ushr 13)
        temp *= -1028477387
        return temp xor (temp ushr 16)
    }

    @JvmStatic
    fun getSeed(position: Vector3i): Long = getSeed(position.x(), position.y(), position.z())

    @JvmStatic
    @Suppress("MagicNumber")
    fun getSeed(x: Int, y: Int, z: Int): Long {
        var seed = (x * 3129871).toLong() xor y.toLong() * 116129781L xor z.toLong()
        seed = seed * seed * 42317861L + seed * 11L
        return seed shr 16
    }

    @JvmStatic
    fun floor(value: Float): Int {
        val result = value.toInt()
        return if (value < result) result - 1 else result
    }

    @JvmStatic
    fun floor(value: Double): Int {
        val result = value.toInt()
        return if (value < result) result - 1 else result
    }

    @JvmStatic
    fun ceil(value: Double): Int {
        val result = value.toInt()
        return if (value > result) result + 1 else result
    }

    @JvmStatic
    @Suppress("MagicNumber")
    fun ceillog2(value: Int): Int {
        val temp = if (GenericMath.isPowerOfTwo(value)) value else GenericMath.roundUpPow2(value)
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(temp.toLong() * 125613361L shr 27 and 31).toInt()]
    }

    @JvmStatic
    fun clamp(value: Int, low: Int, high: Int): Int {
        if (value < low) return low
        if (value > high) return high
        return value
    }

    @JvmStatic
    fun clamp(value: Float, low: Float, high: Float): Float {
        if (value < low) return low
        if (value > high) return high
        return value
    }

    @JvmStatic
    fun clamp(value: Double, low: Double, high: Double): Double {
        if (value < low) return low
        if (value > high) return high
        return value
    }

    @JvmStatic
    fun nextInt(random: Random, low: Int, high: Int): Int {
        if (low >= high) return low
        return random.nextInt(high - low + 1) + low
    }

    @JvmStatic
    fun nextFloat(random: Random, low: Float, high: Float): Float {
        if (low >= high) return low
        return random.nextFloat() * (high - low) + low
    }

    @JvmStatic
    fun randomBetween(random: Random, min: Int, max: Int): Int = random.nextInt(max - min + 1) + min

    @JvmStatic
    @Suppress("MagicNumber")
    fun createInsecureUUID(random: Random): UUID {
        val most = random.nextLong() and -61441L or 16384L
        val least = random.nextLong() and 4611686018427387903L or Long.MIN_VALUE
        return UUID(most, least)
    }
}
