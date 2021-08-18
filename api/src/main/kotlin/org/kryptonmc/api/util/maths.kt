/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmName("Mth")
package org.kryptonmc.api.util

import org.spongepowered.math.GenericMath

@Suppress("MaxLineLength")
private val MULTIPLY_DE_BRUIJN_BIT_POSITION = intArrayOf(0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9)

/**
 * Gets the ceiling of the logarithm of this value, base 2.
 */
@Suppress("MagicNumber")
fun Int.ceillog2(): Int {
    val temp = if (isPowerOfTwo()) this else GenericMath.roundUpPow2(this)
    return MULTIPLY_DE_BRUIJN_BIT_POSITION[(temp.toLong() * 125613361L shr 27 and 31).toInt()]
}

/**
 * Gets the logarithm of this value, base 2.
 */
fun Int.log2() = ceillog2() - if (isPowerOfTwo()) 0 else 1

/**
 * Returns true if this value is a power of two, false otherwise.
 */
fun Int.isPowerOfTwo() = GenericMath.isPowerOfTwo(this)

/**
 * Rounds this value up in powers of two to find the smallest power of two
 * closest to this value.
 */
fun Int.roundUpPow2() = GenericMath.roundUpPow2(this)

/**
 * Rounds this value down to the closest integer (floor function).
 */
fun Float.floor(): Int {
    val result = toInt()
    return if (this < result) result - 1 else result
}

/**
 * Rounds this value down to the closest integer (floor function).
 */
fun Double.floor(): Int {
    val result = toInt()
    return if (this < result) result - 1 else result
}

/**
 * Rounds this value up to the closest integer (ceiling function).
 */
fun Float.ceil(): Int {
    val result = toInt()
    return if (this > result) result + 1 else result
}

/**
 * Rounds this value up to the closest integer (ceiling function).
 */
fun Double.ceil(): Int {
    val result = toInt()
    return if (this > result) result + 1 else result
}
