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
package org.kryptonmc.krypton.world.biome

import com.google.common.hash.Hashing
import org.kryptonmc.api.world.biome.Biome

class BiomeManager(private val source: NoiseBiomeSource, private val seed: Long) {

    fun getBiome(x: Int, y: Int, z: Int): Biome {
        val offsetX = x - ZOOM_BITS
        val offsetY = y - ZOOM_BITS
        val offsetZ = z - ZOOM_BITS
        val shiftedX = offsetX shr ZOOM_BITS
        val shiftedY = offsetY shr ZOOM_BITS
        val shiftedZ = offsetZ shr ZOOM_BITS
        val zoomedX = (offsetX and ZOOM_MASK) / ZOOM
        val zoomedY = (offsetY and ZOOM_MASK) / ZOOM
        val zoomedZ = (offsetZ and ZOOM_MASK) / ZOOM

        var lowestFiddledIndex = 0
        var lowestFiddled = Double.POSITIVE_INFINITY
        for (i in 0 until 8) {
            val perfectX = i and 4 == 0
            val perfectY = i and 2 == 0
            val perfectZ = i and 1 == 0
            val currentX = if (perfectX) shiftedX else shiftedX + 1
            val currentY = if (perfectY) shiftedY else shiftedY + 1
            val currentZ = if (perfectZ) shiftedZ else shiftedZ + 1
            val currentZoomedX = if (perfectX) zoomedX else zoomedX - 1.0
            val currentZoomedY = if (perfectY) zoomedY else zoomedY - 1.0
            val currentZoomedZ = if (perfectZ) zoomedZ else zoomedZ - 1.0
            val fiddled = fiddleDistance(seed, currentX, currentY, currentZ, currentZoomedX, currentZoomedY, currentZoomedZ)
            if (lowestFiddled > fiddled) {
                lowestFiddledIndex = i
                lowestFiddled = fiddled
            }
        }

        val finalX = if (lowestFiddledIndex and 4 == 0) shiftedX else shiftedX + 1
        val finalY = if (lowestFiddledIndex and 2 == 0) shiftedY else shiftedY + 1
        val finalZ = if (lowestFiddledIndex and 1 == 0) shiftedZ else shiftedZ + 1
        return source.getNoiseBiome(finalX, finalY, finalZ)
    }

    companion object {

        private const val ZOOM_BITS = 2
        private const val ZOOM = 4.0
        private const val ZOOM_MASK = 3

        // LCG = Linear Congruential Generator
        private const val LCG_MULTIPLIER = 6364136223846793005L
        private const val LCG_INCREMENT = 1442695040888963407L

        @JvmStatic
        fun obfuscateSeed(seed: Long): Long = Hashing.sha256().hashLong(seed).asLong()

        @JvmStatic
        private fun fiddleDistance(seed: Long, x: Int, y: Int, z: Int, zoomedX: Double, zoomedY: Double, zoomedZ: Double): Double {
            var random = nextRandom(seed, x.toLong())
            random = nextRandom(random, y.toLong())
            random = nextRandom(random, z.toLong())
            random = nextRandom(random, x.toLong())
            random = nextRandom(random, y.toLong())
            random = nextRandom(random, z.toLong())
            val fiddleOne = fiddle(random)
            random = nextRandom(random, seed)
            val fiddleTwo = fiddle(random)
            random = nextRandom(random, seed)
            val fiddleThree = fiddle(random)
            val fiddledX = zoomedX + fiddleOne
            val fiddledY = zoomedY + fiddleTwo
            val fiddledZ = zoomedZ + fiddleThree
            return fiddledZ * fiddledZ + fiddledY * fiddledY + fiddledX * fiddledX
        }

        @JvmStatic
        private fun fiddle(value: Long): Double = ((Math.floorMod(value shr 24, 1024) / 1024.0) - 0.5) * 0.9

        @JvmStatic
        private fun nextRandom(seed: Long, salt: Long): Long = (seed * (seed * LCG_MULTIPLIER * LCG_INCREMENT)) + salt
    }
}
