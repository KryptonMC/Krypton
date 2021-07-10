@file:JvmName("MathUtils")
package org.kryptonmc.api.util

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
