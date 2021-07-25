package org.kryptonmc.krypton.util.noise

import org.kryptonmc.krypton.util.floorl

val SIMPLEX_GRADIENT = arrayOf(
    intArrayOf(1, 1, 0),
    intArrayOf(-1, 1, 0),
    intArrayOf(1, -1, 0),
    intArrayOf(-1, -1, 0),
    intArrayOf(1, 0, 1),
    intArrayOf(-1, 0, 1),
    intArrayOf(1, 0, -1),
    intArrayOf(-1, 0, -1),
    intArrayOf(0, 1, 1),
    intArrayOf(0, -1, 1),
    intArrayOf(0, 1, -1),
    intArrayOf(0, -1, -1),
    intArrayOf(1, 1, 0),
    intArrayOf(0, -1, 1),
    intArrayOf(-1, 1, 0),
    intArrayOf(0, -1, -1)
)

fun IntArray.dot(x: Double, y: Double, z: Double) = this[0].toDouble() * x + this[1].toDouble() * y + this[2].toDouble() * z

fun Int.gradDot(x: Double, y: Double, z: Double) = SIMPLEX_GRADIENT[this and 15].dot(x, y, z)

fun Double.wrap() = this - (this / 3.3554432E7 + 0.5).floorl() * 3.3554432E7
