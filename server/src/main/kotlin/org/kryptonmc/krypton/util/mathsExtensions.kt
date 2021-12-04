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
package org.kryptonmc.krypton.util

import org.spongepowered.math.GenericMath
import java.util.UUID
import kotlin.random.Random

private val MULTIPLY_DE_BRUIJN_BIT_POSITION = intArrayOf(
    0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8,
    31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
)

fun Double.floorl(): Long {
    val value = toLong()
    return if (this < value) value - 1L else value
}

fun Float.floor(): Int {
    val result = toInt()
    return if (this < result) result - 1 else result
}

fun Double.floor(): Int {
    val result = toInt()
    return if (this < result) result - 1 else result
}

fun Double.ceil(): Int {
    val result = toInt()
    return if (this > result) result + 1 else result
}

fun Int.ceillog2(): Int {
    val temp = if (isPowerOfTwo()) this else GenericMath.roundUpPow2(this)
    return MULTIPLY_DE_BRUIJN_BIT_POSITION[(temp.toLong() * 125613361L shr 27 and 31).toInt()]
}

fun Int.log2(): Int = ceillog2() - if (isPowerOfTwo()) 0 else 1

fun Int.isPowerOfTwo(): Boolean = GenericMath.isPowerOfTwo(this)

fun Int.roundToward(toward: Int): Int = (this + toward - 1) / toward * toward

fun Random.nextUUID(): UUID {
    val most = nextLong() and -61441L or 16384L
    val least = nextLong() and 4611686018427387903L or Long.MIN_VALUE
    return UUID(most, least)
}

fun Int.clamp(low: Int, high: Int): Int {
    if (this < low) return low
    if (this > high) return high
    return this
}

fun Double.clamp(low: Double, high: Double): Double {
    if (this < low) return low
    if (this > high) return high
    return this
}

fun Float.clamp(low: Float, high: Float): Float {
    if (this < low) return low
    if (this > high) return high
    return this
}

fun IntRange.sample(random: Random): Int = Maths.randomBetween(random, first, last)

fun IntRange.randomValue(random: Random): Int {
    if (first == last) return first
    return random.nextInt(last - first + 1) + first
}

fun Int.toArea(): Int = (this * 2 + 1).square()

fun Int.square(): Int = this * this

fun Double.fade(): Double = this * this * this * (this * (this * 6 - 15) + 10)

fun Double.clampedLerp(lower: Double, upper: Double): Double {
    if (this < lower) return lower
    if (this > upper) return upper
    return GenericMath.lerp(lower, upper, this)
}

fun Random.nextFloatClamped(a: Float, b: Float): Float {
    if (a >= b) return a
    return nextFloat() * (b - a) + a
}
