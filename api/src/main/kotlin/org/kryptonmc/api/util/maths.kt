/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmName("MathUtils")
package org.kryptonmc.api.util

import org.spongepowered.math.GenericMath

private val MULTIPLY_DE_BRUIJN_BIT_POSITION = intArrayOf(0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9)

fun Int.ceillog2(): Int {
    val temp = if (isPowerOfTwo()) this else GenericMath.roundUpPow2(this)
    return MULTIPLY_DE_BRUIJN_BIT_POSITION[(temp.toLong() * 125613361L shr 27 and 31).toInt()]
}

fun Int.log2() = ceillog2() - if (isPowerOfTwo()) 0 else 1

fun Int.isPowerOfTwo() = GenericMath.isPowerOfTwo(this)

fun Int.roundUpPow2() = GenericMath.roundUpPow2(this)

fun Float.floor(): Int {
    val result = toInt()
    return if (this < result) result - 1 else result
}

fun Double.floor(): Int {
    val result = toInt()
    return if (this < result) result - 1 else result
}

fun Float.ceil(): Int {
    val result = toInt()
    return if (this > result) result + 1 else result
}

fun Double.ceil(): Int {
    val result = toInt()
    return if (this > result) result + 1 else result
}
