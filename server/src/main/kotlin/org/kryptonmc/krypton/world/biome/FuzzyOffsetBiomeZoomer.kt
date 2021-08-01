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
package org.kryptonmc.krypton.world.biome

import org.kryptonmc.api.space.square
import org.kryptonmc.krypton.util.LinearCongruentialGenerator

object FuzzyOffsetBiomeZoomer : BiomeZoomer {

    private const val BITS = 2
    private const val ZOOM = 4.0
    private const val MASK = 3

    override fun invoke(seed: Long, x: Int, y: Int, z: Int, source: NoiseBiomeSource): KryptonBiome {
        val xOff = x - BITS
        val yOff = y - BITS
        val zOff = z - BITS
        val xBits = xOff shr BITS
        val yBits = yOff shr BITS
        val zBits = zOff shr BITS
        val zoomedX = (xOff and MASK) / ZOOM
        val zoomedY = (yOff and MASK) / ZOOM
        val zoomedZ = (zOff and MASK) / ZOOM
        var lowestFiddledIndex = 0
        var lowestFiddled = Double.POSITIVE_INFINITY
        for (i in 0 until 8) {
            val hasA = (i and 4) == 0
            val hasB = (i and 2) == 0
            val hasC = (i and 1) == 0
            val currentX = if (hasA) xBits else xBits + 1
            val currentY = if (hasB) yBits else yBits + 1
            val currentZ = if (hasC) zBits else zBits + 1
            val currentZoomedX = if (hasA) zoomedX else zoomedX - 1.0
            val currentZoomedY = if (hasB) zoomedY else zoomedY - 1.0
            val currentZoomedZ = if (hasC) zoomedZ else zoomedZ - 1.0
            val fiddledDistance = fiddledDistance(seed, currentX, currentY, currentZ, currentZoomedX, currentZoomedY, currentZoomedZ)
            if (lowestFiddled > fiddledDistance) {
                lowestFiddledIndex = i
                lowestFiddled = fiddledDistance
            }
        }
        val a = if (lowestFiddledIndex and 4 == 0) xBits else xBits + 1
        val b = if (lowestFiddledIndex and 2 == 0) yBits else yBits + 1
        val c = if (lowestFiddledIndex and 1 == 0) zBits else zBits + 1
        return source[a, b, c]
    }
}

private fun fiddledDistance(seed: Long, x: Int, y: Int, z: Int, maskedX: Double, maskedY: Double, maskedZ: Double): Double {
    var random = LinearCongruentialGenerator.next(seed, x.toLong())
    random = LinearCongruentialGenerator.next(random, y.toLong())
    random = LinearCongruentialGenerator.next(random, z.toLong())
    random = LinearCongruentialGenerator.next(random, x.toLong())
    random = LinearCongruentialGenerator.next(random, y.toLong())
    random = LinearCongruentialGenerator.next(random, z.toLong())
    val fiddleOne = fiddle(random)
    random = LinearCongruentialGenerator.next(random, seed)
    val fiddleTwo = fiddle(random)
    random = LinearCongruentialGenerator.next(random, seed)
    val fiddleThree = fiddle(random)
    return (maskedZ + fiddleThree).square() + (maskedY + fiddleTwo).square() + (maskedX + fiddleThree).square()
}

private fun fiddle(seed: Long): Double {
    val floorMod = Math.floorMod(seed shr 24, 1024) / 1024.0
    return (floorMod - 0.5) * 0.9
}
