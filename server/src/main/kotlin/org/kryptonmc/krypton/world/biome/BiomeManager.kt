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

import com.google.common.hash.Hashing
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.util.LinearCongruentialGenerator
import org.kryptonmc.krypton.util.Quart
import org.kryptonmc.krypton.util.floor
import org.spongepowered.math.vector.Vector3i

class BiomeManager(
    private val source: NoiseBiomeSource,
    private val seed: Long
) {

    operator fun get(x: Int, y: Int, z: Int): Biome {
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

    fun getNoiseBiome(x: Double, y: Double, z: Double): Biome {
        val quartX = Quart.fromBlock(x.floor())
        val quartY = Quart.fromBlock(y.floor())
        val quartZ = Quart.fromBlock(z.floor())
        return getNoiseBiome(quartX, quartY, quartZ)
    }

    fun getNoiseBiome(position: Vector3i): Biome {
        val quartX = Quart.fromBlock(position.x())
        val quartY = Quart.fromBlock(position.y())
        val quartZ = Quart.fromBlock(position.z())
        return getNoiseBiome(quartX, quartY, quartZ)
    }

    private fun getNoiseBiome(x: Int, y: Int, z: Int) = source.getNoiseBiome(x, y, z)

    companion object {

        const val CENTER_QUART = 8 shr Quart.BITS
        private const val ZOOM_BITS = 2
        private const val ZOOM = 4.0
        private const val ZOOM_MASK = 3

        @JvmStatic
        fun obfuscateSeed(seed: Long): Long = Hashing.sha256().hashLong(seed).asLong()

        @JvmStatic
        private fun fiddleDistance(seed: Long, x: Int, y: Int, z: Int, zoomedX: Double, zoomedY: Double, zoomedZ: Double): Double {
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
            val fiddledX = zoomedX + fiddleOne
            val fiddledY = zoomedY + fiddleTwo
            val fiddledZ = zoomedZ + fiddleThree
            return fiddledZ * fiddledZ + fiddledY * fiddledY + fiddledX * fiddledX
        }

        @JvmStatic
        private fun fiddle(value: Long): Double = ((Math.floorMod(value shr 24, 1024) / 1024.0) - 0.5) * 0.9
    }
}
