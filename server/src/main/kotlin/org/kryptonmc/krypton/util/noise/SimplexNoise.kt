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
package org.kryptonmc.krypton.util.noise

import org.kryptonmc.krypton.util.floor
import org.kryptonmc.krypton.util.random.RandomSource
import kotlin.math.sqrt

class SimplexNoise(random: RandomSource) {

    private val permutations = IntArray(512) { it }
    val xOffset: Double = random.nextDouble() * 256
    val yOffset: Double = random.nextDouble() * 256
    val zOffset: Double = random.nextDouble() * 256

    init {
        // Randomise the permutation table
        for (i in 0 until 256) {
            val offset = random.nextInt(256 - i)
            val old = permutations[i]
            permutations[i] = permutations[i + offset]
            permutations[i + offset] = old
        }
    }

    // 2D simplex noise
    @Suppress("LocalVariableName")
    fun getValue(x: Double, y: Double): Double {
        val s = (x + y) * F2 // Hairy factor for 2D
        val i = (x + s).floor()
        val j = (y + s).floor()
        val t = (i + j) * G2
        val X0 = i - t // Unskew the cell origin back to (x,y) space
        val Y0 = j - t
        val x0 = x - X0 // The x,y distances from the cell origin
        val y0 = y - Y0
        // For the 2D case, the simplex shape is an equilateral triangle.
        // Determine which simplex we are in.
        // Offsets for second (middle) corner of simplex in (i,j) coords
        // if x0 > y0, lower triangle, XY order: (0,0)->(1,0)->(1,1)
        // if x0 <= y0 upper triangle, YX order: (0,0)->(0,1)->(1,1)
        val i1 = if (x0 > y0) 1 else 0
        val j1 = if (x0 > y0) 0 else 1
        // A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
        // a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
        // c = (3-sqrt(3))/6
        val x1 = x0 - i1 + G2
        val y1 = y0 - j1 + G2
        val x2 = x0 - 1.0 + 2.0 * G2
        val y2 = y0 - 1.0 + 2.0 * G2
        // Work out the hashed gradient indices of the three simplex corners
        val ii = i and 255
        val jj = j and 255
        val gi0 = permute(ii + permute(jj)) % 12
        val gi1 = permute(ii + i1 + permute(jj + j1)) % 12
        val gi2 = permute(ii + 1 + permute(jj + 1)) % 12
        // Calculate the contribution from the three corners
        val c0 = getCornerNoise3D(gi0, x0, y0, 0.0, 0.5)
        val c1 = getCornerNoise3D(gi1, x1, y1, 0.0, 0.5)
        val c2 = getCornerNoise3D(gi2, x2, y2, 0.0, 0.5)
        return 70.0 * (c0 + c1 + c2)
    }

    // 3D simplex noise
    @Suppress("LocalVariableName")
    fun getValue(x: Double, y: Double, z: Double): Double {
        // Skew the input space to determine which simplex cell we're in
        val s = (x + y + z) * F3 // Very nice and simple skew factor for 3D
        val i = (x + s).floor()
        val j = (y + s).floor()
        val k = (z + s).floor()
        val t = (i + j + k) * G3
        // Unskew the cell origin back to (x,y,z) space
        val X0 = i - t
        val Y0 = j - t
        val Z0 = k - t
        // The x,y,z distances from the cell origin
        val x0 = x - X0
        val y0 = y - Y0
        val z0 = z - Z0
        // For the 3D case, the simplex shape is a slightly irregular tetrahedron.
        // Determine which simplex we are in.
        // Offsets for second corner of simplex in (i,j,k) coords
        val i1: Int
        val j1: Int
        val k1: Int
        // Offsets for third corner of simplex in (i,j,k) coords
        val i2: Int
        val j2: Int
        val k2: Int
        when {
            x0 >= y0 -> when {
                y0 >= z0 -> { // X Y Z order
                    i1 = 1
                    j1 = 0
                    k1 = 0
                    i2 = 1
                    j2 = 1
                    k2 = 0
                }
                x0 >= z0 -> { // X Z Y order
                    i1 = 1
                    j1 = 0
                    k1 = 0
                    i2 = 1
                    j2 = 0
                    k2 = 1
                }
                else -> { // Z X Y order
                    i1 = 0
                    j1 = 0
                    k1 = 1
                    i2 = 1
                    j2 = 0
                    k2 = 1
                }
            }
            y0 < z0 -> { // Z Y X order
                i1 = 0
                j1 = 0
                k1 = 1
                i2 = 0
                j2 = 1
                k2 = 1
            }
            x0 < z0 -> { // Y Z X order
                i1 = 0
                j1 = 1
                k1 = 0
                i2 = 0
                j2 = 1
                k2 = 1
            }
            else -> { // Y X Z order
                i1 = 0
                j1 = 1
                k1 = 0
                i2 = 1
                j2 = 1
                k2 = 0
            }
        }
        // A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
        // a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z), and
        // a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z), where
        // c = 1/6.
        // Offsets for second corner in (x,y,z) coords
        val x1 = x0 - i1 + G3
        val y1 = y0 - j1 + G3
        val z1 = z0 - k1 + G3
        // Offsets for third corner in (x,y,z) coords
        val x2 = x0 - i2 + 2.0 * G3
        val y2 = y0 - j2 + 2.0 * G3
        val z2 = z0 - k2 + 2.0 * G3
        // Offsets for last corner in (x,y,z) coords
        val x3 = x0 - 1.0 + 3.0 * G3
        val y3 = y0 - 1.0 + 3.0 * G3
        val z3 = z0 - 1.0 + 3.0 * G3
        // Work out the hashed gradient indices of the four simplex corners
        val ii = i and 255
        val jj = j and 255
        val kk = k and 255
        val gi0 = permute(ii + permute(jj + permute(kk))) % 12
        val gi1 = permute(ii + i1 + permute(jj + j1 + permute(kk + k1))) % 12
        val gi2 = permute(ii + i2 + permute(jj + j2 + permute(kk + k2))) % 12
        val gi3 = permute(ii + 1 + permute(jj + 1 + permute(kk + 1))) % 12
        val c0 = getCornerNoise3D(gi0, x0, y0, z0, 0.6)
        val c1 = getCornerNoise3D(gi1, x1, y1, z1, 0.6)
        val c2 = getCornerNoise3D(gi2, x2, y2, z2, 0.6)
        val c3 = getCornerNoise3D(gi3, x3, y3, z3, 0.6)
        return 32.0 * (c0 + c1 + c2 + c3)
    }

    private fun getCornerNoise3D(hash: Int, x: Double, y: Double, z: Double, distance: Double): Double {
        var distOff = distance - x * x - y * y - z * z
        return if (distOff < 0.0) {
            0.0
        } else {
            distOff *= distOff
            distOff * distOff * GRADIENT[hash].dot(x, y, z)
        }
    }

    private fun permute(hash: Int): Int = permutations[hash and 255]

    companion object {

        private val SQRT_3 = sqrt(3.0)
        private val F2 = 0.5 * (SQRT_3 - 1.0)
        private val G2 = (3.0 - SQRT_3) / 6.0
        private const val F3 = 1.0 / 3.0
        const val G3: Double = 1.0 / 6.0

        @JvmField
        val GRADIENT: Array<IntArray> = arrayOf(
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
    }
}
