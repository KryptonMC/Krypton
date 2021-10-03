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

import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.util.fade
import org.kryptonmc.krypton.util.floor
import org.kryptonmc.krypton.util.random.RandomSource

class ImprovedNoise(random: RandomSource) {

    private val permutations = ByteArray(256) { it.toByte() }
    val xOffset = random.nextDouble() * 256
    val yOffset = random.nextDouble() * 256
    val zOffset = random.nextDouble() * 256

    init {
        // Randomise the permutation table
        for (i in 0 until 256) {
            val offset = random.nextInt(256 - i)
            val old = permutations[i]
            permutations[i] = permutations[i + offset]
            permutations[i + offset] = old
        }
    }

    fun noise(x: Double, y: Double, z: Double, yScale: Double = 0.0, yMax: Double = 0.0): Double {
        val offX = x + xOffset
        val offY = y + yOffset
        val offZ = z + zOffset
        val floorX = offX.floor()
        val floorY = offY.floor()
        val floorZ = offZ.floor()
        val relX = offX - floorX
        val fadeRelX = offY - floorY
        val relZ = offZ - floorZ
        val scaleY = if (yScale != 0.0) {
            val max = if (yMax >= 0.0 && yMax < fadeRelX) yMax else fadeRelX
            (max / yScale + SHIFT_UP_EPSILON).floor() * yScale
        } else 0.0
        val relY = fadeRelX - scaleY
        val a = permute(floorX)
        val aa = permute(floorX + 1)
        val b = permute(a + floorY)
        val bb = permute(a + floorY + 1)
        val c = permute(aa + floorY)
        val cc = permute(aa + floorY + 1)
        val d = permute(b + floorZ).gradDot(relX, relY, relZ)
        val e = permute(c + floorZ).gradDot(relX - 1, relY, relZ)
        val f = permute(bb + floorZ).gradDot(relX, relY - 1, relZ)
        val g = permute(cc + floorZ).gradDot(relX - 1, relY - 1, relZ)
        val h = permute(b + floorZ + 1).gradDot(relX, relY, relZ - 1)
        val i = permute(c + floorZ + 1).gradDot(relX - 1, relY, relZ - 1)
        val j = permute(bb + floorZ + 1).gradDot(relX, relY - 1, relZ - 1)
        val k = permute(cc + floorZ + 1).gradDot(relX - 1, relY - 1, relZ - 1)
        val fadeX = relX.fade()
        val fadeY = fadeRelX.fade()
        val fadeZ = relZ.fade()
        return Maths.triLerp(fadeX, fadeY, fadeZ, d, e, f, g, h, i, j, k)
    }

    private fun permute(hash: Int) = permutations[hash and 255].toInt() and 255

    companion object {

        private const val SHIFT_UP_EPSILON = 1.0E-7F

        private fun Int.gradDot(x: Double, y: Double, z: Double) = SimplexNoise.GRADIENT[this and 15].dot(x, y, z)
    }
}
