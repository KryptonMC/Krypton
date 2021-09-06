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

fun Double.frac() = this - floorl().toDouble()

fun Int.ceillog2(): Int {
    val temp = if (isPowerOfTwo()) this else GenericMath.roundUpPow2(this)
    return MULTIPLY_DE_BRUIJN_BIT_POSITION[(temp.toLong() * 125613361L shr 27 and 31).toInt()]
}

fun Int.log2() = ceillog2() - if (isPowerOfTwo()) 0 else 1

fun Int.isPowerOfTwo() = GenericMath.isPowerOfTwo(this)

fun Int.roundToward(toward: Int) = (this + toward - 1) / toward * toward

fun Random.nextUUID(): UUID {
    val most = nextLong() and -61441L or 16384L
    val least = nextLong() and 4611686018427387903L or Long.MIN_VALUE
    return UUID(most, least)
}

fun Int.clamp(low: Int, high: Int) = if (this < low) low else if (this > high) high else this

fun Double.clamp(low: Double, high: Double) = if (this < low) low else if (this > high) high else this

fun Float.clamp(low: Float, high: Float) = if (this < low) low else if (this > high) high else this

fun nextInt(random: Random, lower: Int, upper: Int): Int {
    if (lower >= upper) return lower
    return random.nextInt(upper - lower + 1) + lower
}

fun Int.toArea() = (this * 2 + 1).square()

fun Int.square() = this * this

fun Double.fade() = this * this * this * (this * (this * 6 - 15) + 10)

fun Double.clampedLerp(lower: Double, upper: Double): Double {
    if (this < lower) return lower
    if (this > upper) return upper
    return GenericMath.lerp(lower, upper, this)
}

fun java.util.Random.nextFloat(a: Float, b: Float) = if (a >= b) a else nextFloat() * (b - a) + a
