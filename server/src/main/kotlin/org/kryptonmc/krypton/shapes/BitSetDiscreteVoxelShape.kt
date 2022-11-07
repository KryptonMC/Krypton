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
package org.kryptonmc.krypton.shapes

import org.kryptonmc.api.util.Direction
import java.util.BitSet
import kotlin.math.max
import kotlin.math.min

class BitSetDiscreteVoxelShape : DiscreteVoxelShape {

    private val storage: BitSet
    private var minimumX = 0
    private var minimumY = 0
    private var minimumZ = 0
    private var maximumX = 0
    private var maximumY = 0
    private var maximumZ = 0

    constructor(sizeX: Int, sizeY: Int, sizeZ: Int) : super(sizeX, sizeY, sizeZ) {
        storage = BitSet(sizeX * sizeY * sizeZ)
        minimumX = sizeX
        minimumY = sizeY
        minimumZ = sizeZ
    }

    constructor(shape: DiscreteVoxelShape) : super(shape.sizeX, shape.sizeY, shape.sizeZ) {
        if (shape is BitSetDiscreteVoxelShape) {
            storage = shape.storage.clone() as BitSet
        } else {
            storage = BitSet(sizeX * sizeY * sizeZ)
            for (x in 0 until sizeX) {
                for (y in 0 until sizeY) {
                    for (z in 0 until sizeZ) {
                        if (shape.isFull(x, y, z)) storage.set(getIndex(x, y, z))
                    }
                }
            }
        }
        minimumX = shape.firstFull(Direction.Axis.X)
        minimumY = shape.firstFull(Direction.Axis.Y)
        minimumZ = shape.firstFull(Direction.Axis.Z)
        maximumX = shape.lastFull(Direction.Axis.X)
        maximumY = shape.lastFull(Direction.Axis.Y)
        maximumZ = shape.lastFull(Direction.Axis.Z)
    }

    override fun isFull(x: Int, y: Int, z: Int): Boolean = storage.get(getIndex(x, y, z))

    override fun fill(x: Int, y: Int, z: Int) {
        fillUpdateBounds(x, y, z, true)
    }

    override fun firstFull(axis: Direction.Axis): Int = axis.select(minimumX, minimumY, minimumZ)

    override fun lastFull(axis: Direction.Axis): Int = axis.select(maximumX, maximumY, maximumZ)

    override fun isEmpty(): Boolean = storage.isEmpty

    private fun fillUpdateBounds(x: Int, y: Int, z: Int, update: Boolean) {
        storage.set(getIndex(x, y, z))
        if (update) {
            minimumX = min(minimumX, x)
            minimumY = min(minimumY, y)
            minimumZ = min(minimumZ, z)
            maximumX = max(maximumX, x + 1)
            maximumY = max(maximumY, y + 1)
            maximumZ = max(maximumZ, z + 1)
        }
    }

    private fun getIndex(x: Int, y: Int, z: Int): Int = (x * sizeY + y) * sizeZ + z

    private fun isZStripFull(minimumZ: Int, maximumZ: Int, x: Int, y: Int): Boolean {
        if (x < sizeX && y < sizeY) return storage.nextClearBit(getIndex(x, y, minimumZ)) >= getIndex(x, y, maximumZ)
        return false
    }

    private fun isXZRectangleFull(minimumX: Int, maximumX: Int, minimumZ: Int, maximumZ: Int, y: Int): Boolean {
        for (x in minimumX until maximumX) {
            if (!isZStripFull(minimumZ, maximumZ, x, y)) return false
        }
        return true
    }

    private fun clearZStrip(minimumZ: Int, maximumZ: Int, x: Int, y: Int) {
        storage.clear(getIndex(x, y, minimumZ), getIndex(x, y, maximumZ))
    }

    override fun toString(): String = "BitSetDiscreteVoxelShape(sizeX=$sizeX, sizeY=$sizeY, sizeZ=$sizeZ, storage=$storage, minimumX=$minimumX, " +
            "minimumY=$minimumY, minimumZ=$minimumZ, maximumX=$maximumX, maximumY=$maximumY, maximumZ=$maximumZ)"

    companion object {

        @JvmStatic
        fun withFilledBounds(x: Int, y: Int, z: Int, minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int): BitSetDiscreteVoxelShape {
            val shape = BitSetDiscreteVoxelShape(x, y, z)
            shape.minimumX = minX
            shape.minimumY = minY
            shape.minimumZ = minZ
            shape.maximumX = maxX
            shape.maximumY = maxY
            shape.maximumZ = maxZ
            for (xOff in minX until maxX) {
                for (yOff in minY until maxY) {
                    for (zOff in minZ until maxZ) {
                        shape.fillUpdateBounds(xOff, yOff, zOff, false)
                    }
                }
            }
            return shape
        }

        @JvmStatic
        @Suppress("MagicNumber")
        fun join(
            mainShape: DiscreteVoxelShape,
            secondaryShape: DiscreteVoxelShape,
            mergerX: IndexMerger,
            mergerY: IndexMerger,
            mergerZ: IndexMerger,
            operator: BooleanOperator
        ): BitSetDiscreteVoxelShape {
            val shape = BitSetDiscreteVoxelShape(mergerX.size - 1, mergerY.size - 1, mergerZ.size - 1)
            // ordered like so: minX, minY, minZ, maxX, maxY, maxZ
            val bounds = intArrayOf(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
            mergerX.forMergedIndices { x1, x2, x3 ->
                var hasX = false
                mergerY.forMergedIndices { y1, y2, y3 ->
                    var hasY = false
                    mergerZ.forMergedIndices { z1, z2, z3 ->
                        if (operator.apply(mainShape.isFullWide(x1, y1, z1), secondaryShape.isFullWide(x2, y2, z2))) {
                            shape.storage.set(shape.getIndex(x3, y3, z3))
                            bounds[2] = min(bounds[2], z3)
                            bounds[5] = max(bounds[5], z3)
                            hasY = true
                        }
                        true
                    }
                    if (hasY) {
                        bounds[1] = min(bounds[1], y3)
                        bounds[4] = max(bounds[4], y3)
                        hasX = true
                    }
                    true
                }
                if (hasX) {
                    bounds[0] = min(bounds[0], x3)
                    bounds[3] = max(bounds[3], x3)
                }
                true
            }
            shape.minimumX = bounds[0]
            shape.minimumY = bounds[1]
            shape.minimumZ = bounds[2]
            shape.maximumX = bounds[3] + 1
            shape.maximumY = bounds[4] + 1
            shape.maximumZ = bounds[5] + 1
            return shape
        }

        @JvmStatic
        fun forAllBoxes(shape: DiscreteVoxelShape, consumer: IntLineConsumer, combine: Boolean) {
            val newShape = BitSetDiscreteVoxelShape(shape)
            for (y in 0 until newShape.sizeY) {
                for (x in 0 until newShape.sizeX) {
                    var minimumZ = -1
                    for (z in 0..newShape.sizeZ) {
                        if (newShape.isFullWide(x, y, z)) {
                            if (combine) {
                                if (minimumZ == -1) minimumZ = z
                            } else {
                                consumer.consume(x, y, z, x + 1, y + 1, z + 1)
                            }
                        } else if (minimumZ != -1) {
                            var maximumX = x
                            var maximumY = y
                            newShape.clearZStrip(minimumZ, z, x, y)
                            while (newShape.isZStripFull(minimumZ, z, maximumX + 1, y)) {
                                newShape.clearZStrip(minimumZ, z, maximumX + 1, y)
                                maximumX++
                            }
                            while (newShape.isXZRectangleFull(x, maximumX + 1, minimumZ, z, maximumY + 1)) {
                                for (i in x..maximumX) {
                                    newShape.clearZStrip(minimumZ, z, i, maximumY + 1)
                                }
                                maximumY++
                            }
                            consumer.consume(x, y, minimumZ, maximumX + 1, maximumY + 1, z)
                            minimumZ = -1
                        }
                    }
                }
            }
        }
    }
}
