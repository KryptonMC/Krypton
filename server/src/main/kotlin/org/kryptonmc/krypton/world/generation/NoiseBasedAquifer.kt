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
package org.kryptonmc.krypton.world.generation

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.util.floor
import org.kryptonmc.krypton.util.asLong
import org.kryptonmc.krypton.util.noise.NormalNoise
import org.kryptonmc.krypton.util.random.WorldGenRandom
import org.kryptonmc.krypton.util.seed
import org.kryptonmc.krypton.util.x
import org.kryptonmc.krypton.util.y
import org.kryptonmc.krypton.util.z
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.generation.noise.NoiseGeneratorSettings
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class NoiseBasedAquifer(
    position: ChunkPosition,
    private val barrierNoise: NormalNoise,
    private val waterLevelNoise: NormalNoise,
    private val lavaNoise: NormalNoise,
    private val noiseGeneratorSettings: NoiseGeneratorSettings,
    private val sampler: NoiseSampler,
    startY: Int,
    deltaY: Int
) : Aquifer {

    private val minGridX = gridX(position.minBlockX) - 1
    private val minGridY = gridY(startY) - 1
    private val minGridZ = gridZ(position.minBlockZ) - 1
    private val gridSizeX = (gridX(position.maxBlockX) + 1) - minGridX + 1
    private val gridSizeZ = (gridZ(position.maxBlockZ) + 1) - minGridZ + 1
    private val aquifierCache: Array<AquifierStatus?>
    private val aquifierLocationCache: LongArray
    override var shouldScheduleFluidUpdate = false
        private set

    init {
        val gridDiffY = gridY(startY + deltaY) + 1
        val gridSizeY = gridDiffY - minGridY + 1
        val size = gridSizeX * gridSizeY * gridSizeZ
        aquifierCache = arrayOfNulls(size)
        aquifierLocationCache = LongArray(size) { Long.MAX_VALUE }
    }

    override fun computeState(stoneSource: StoneSource, x: Int, y: Int, z: Int, value: Double): Block {
        if (value <= 0.0) {
            val scale: Double
            val block: Block
            val isWater: Boolean
            if (y.isLavaLevel()) {
                scale = 0.0
                block = Blocks.LAVA
                isWater = false
            } else {
                val xFloor = (x - 5).floorDiv(16)
                val yFloor = (y + 1).floorDiv(12)
                val zFloor = (z - 5).floorDiv(16)
                var distSq1 = Int.MAX_VALUE
                var distSq2 = Int.MAX_VALUE
                var distSq3 = Int.MAX_VALUE
                var loc1 = 0L
                var loc2 = 0L
                var loc3 = 0L
                for (xo in 0..1) {
                    for (yo in -1..1) {
                        for (zo in 0..1) {
                            val xOff = xFloor + xo
                            val yOff = yFloor + yo
                            val zOff = zFloor + zo
                            val index = indexOf(xOff, yOff, zOff)
                            val cached = aquifierLocationCache[index]
                            val location = if (cached != Long.MAX_VALUE) {
                                cached
                            } else {
                                val random = WorldGenRandom(seed(xOff, yOff * 3, zOff) + 1L)
                                asLong(xOff * 16 + random.nextInt(10), yOff * 12 + random.nextInt(9), zOff * 16 + random.nextInt(10)).apply { aquifierLocationCache[index] = this }
                            }
                            val xDiff = location.x - x
                            val yDiff = location.y - y
                            val zDiff = location.z - z
                            val distanceSquared = xDiff * xDiff + yDiff * yDiff + zDiff * zDiff
                            if (distSq1 >= distanceSquared) {
                                loc3 = loc2
                                loc2 = location
                                loc1 = location
                                distSq3 = distSq2
                                distSq2 = distSq1
                                distSq1 = distanceSquared
                            } else if (distSq2 >= distanceSquared) {
                                loc3 = loc2
                                loc2 = location
                                distSq3 = distSq2
                                distSq2 = distanceSquared
                            } else if (distSq3 >= distanceSquared) {
                                loc3 = location
                                distSq3 = distanceSquared
                            }
                        }
                    }
                }
                val status1 = loc1.toStatus()
                val status2 = loc2.toStatus()
                val status3 = loc3.toStatus()
                val similarity1 = similarity(distSq1, distSq2)
                val similarity2 = similarity(distSq1, distSq3)
                val similarity3 = similarity(distSq2, distSq3)
                isWater = similarity1 > 0.0
                scale = if (status1.level >= y && status1.type == Blocks.WATER && (y - 1).isLavaLevel()) {
                    1.0
                } else if (similarity1 > -1.0) {
                    val barrier = 1.0 + (barrierNoise.getValue(x.toDouble(), y.toDouble(), z.toDouble()) + 0.05) / 4.0
                    val pressure1 = calculatePressure(y, barrier, status1, status2)
                    val pressure2 = calculatePressure(y, barrier, status1, status3)
                    val pressure3 = calculatePressure(y, barrier, status2, status3)
                    val max1 = max(0.0, similarity1)
                    val max2 = max(0.0, similarity2)
                    val max3 = max(0.0, similarity3)
                    val whatever = 2.0 * max1 * max(pressure1, max(pressure2 * max2, pressure3 * max3))
                    max(0.0, whatever)
                } else {
                    0.0
                }
                block = if (y >= status1.level) Blocks.AIR else status1.type
            }
            if (value + scale <= 0.0) {
                shouldScheduleFluidUpdate = isWater
                return block
            }
        }
        shouldScheduleFluidUpdate = false
        return stoneSource[x, y, z]
    }

    private fun gridX(x: Int) = x.floorDiv(16)

    private fun gridY(y: Int) = y.floorDiv(12)

    private fun gridZ(z: Int) = z.floorDiv(16)

    private fun indexOf(x: Int, y: Int, z: Int): Int {
        val diffX = x - minGridX
        val diffY = y - minGridY
        val diffZ = z - minGridZ
        return (diffY * gridSizeZ + diffZ) * gridSizeX + diffX
    }

    private fun similarity(a: Int, b: Int): Double = 1.0 - abs(b - a).toDouble() / 25.0

    private fun calculatePressure(y: Int, noise: Double, first: AquifierStatus, second: AquifierStatus): Double {
        if (y <= first.level && y <= second.level && first.type != second.type) return 1.0
        val levelDiff = abs(first.level - second.level)
        val factored = 0.5 * (first.level + second.level).toDouble()
        val value = abs(factored - y.toDouble() - 0.5)
        return 0.5 * levelDiff.toDouble() * noise * value
    }

    private fun computeAquifier(x: Int, y: Int, z: Int): AquifierStatus {
        val seaLevel = noiseGeneratorSettings.seaLevel
        if (y > Aquifer.ALWAYS_USE_SEA_LEVEL_WHEN_ABOVE) return AquifierStatus(seaLevel, Blocks.WATER)
        var waterValue = waterLevelNoise.getValue(x.floorDiv(64).toDouble(), y.floorDiv(40).toDouble() / 1.4, z.floorDiv(64).toDouble()) * 30.0 + -10.0
        var isLava = false
        if (abs(waterValue) > 8.0) waterValue += 4.0
        val flooredY = y.floorDiv(40) * 40 + 20
        val floor = flooredY + waterValue.floor()
        if (flooredY == -20) {
            val lavaValue = lavaNoise.getValue(x.floorDiv(64).toDouble(), y.floorDiv(40).toDouble() / 1.4, z.floorDiv(64).toDouble())
            isLava = abs(lavaValue) > 0.22
        }
        return AquifierStatus(min(56, floor), if (isLava) Blocks.LAVA else Blocks.WATER)
    }

    private fun Int.isLavaLevel() = this - noiseGeneratorSettings.noiseSettings.minimumY <= Aquifer.ALWAYS_LAVA_AT_OR_BELOW_Y_INDEX

    private fun Long.toStatus(): AquifierStatus {
        val x = x
        val y = y
        val z = z
        val gridX = gridX(x)
        val gridY = gridY(y)
        val gridZ = gridZ(z)
        val index = indexOf(gridX, gridY, gridZ)
        val status = aquifierCache[index]
        if (status != null) return status
        val aquifier = computeAquifier(x, y, z)
        aquifierCache[index] = aquifier
        return aquifier
    }

    class AquifierStatus(val level: Int, val type: Block)
}
