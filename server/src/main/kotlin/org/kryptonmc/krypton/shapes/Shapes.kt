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

import it.unimi.dsi.fastutil.doubles.DoubleArrayList
import it.unimi.dsi.fastutil.doubles.DoubleList
import org.kryptonmc.api.util.Direction.Axis
import org.kryptonmc.krypton.util.Maths
import java.util.Objects
import kotlin.math.abs
import kotlin.math.roundToInt

object Shapes {

    const val EPSILON: Double = 1.0E-7
    private const val P_INFINITY = Double.POSITIVE_INFINITY
    private const val N_INFINITY = Double.NEGATIVE_INFINITY
    private val BLOCK = CubeVoxelShape(BitSetDiscreteVoxelShape(1, 1, 1).apply { fill(0, 0, 0) })
    private val EMPTY = ArrayVoxelShape(BitSetDiscreteVoxelShape(0, 0, 0), emptyList(), emptyList(), emptyList())
    @JvmField
    val INFINITY: VoxelShape = box(N_INFINITY, N_INFINITY, N_INFINITY, P_INFINITY, P_INFINITY, P_INFINITY)

    @JvmStatic
    fun empty(): VoxelShape = EMPTY

    @JvmStatic
    fun block(): VoxelShape = BLOCK

    @JvmStatic
    fun box(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): VoxelShape {
        require(minX <= maxX && minY <= maxY && minZ <= maxZ) {
            "Invalid bounds! Minimums must be smaller than maximums! Minimums: $minX, $minY, $minZ, Maximums: $maxX, $maxY, $maxZ"
        }
        return create(minX, minY, minZ, maxX, maxY, maxZ)
    }

    @JvmStatic
    fun create(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): VoxelShape {
        @Suppress("SimplifyNegatedBinaryExpression") // Any of these could be NaN
        if (!(!(maxX - minX < EPSILON) && !(maxY - minY < EPSILON) && !(maxZ - minZ < EPSILON))) return empty()
        val bitsX = findBits(minX, maxX)
        val bitsY = findBits(minY, maxY)
        val bitsZ = findBits(minZ, maxZ)
        if (!(bitsX >= 0 && bitsY >= 0 && bitsZ >= 0)) {
            return ArrayVoxelShape(BLOCK.shape, minMaxList(minX, maxX), minMaxList(minY, maxY), minMaxList(minZ, maxZ))
        }
        if (bitsX == 0 && bitsY == 0 && bitsZ == 0) return block()
        val x = 1 shl bitsX
        val y = 1 shl bitsY
        val z = 1 shl bitsZ
        val minimumX = (minX * x.toDouble()).roundToInt()
        val minimumY = (minY * y.toDouble()).roundToInt()
        val minimumZ = (minZ * z.toDouble()).roundToInt()
        val maximumX = (maxX * x.toDouble()).roundToInt()
        val maximumY = (maxY * y.toDouble()).roundToInt()
        val maximumZ = (maxZ * z.toDouble()).roundToInt()
        return CubeVoxelShape(BitSetDiscreteVoxelShape.withFilledBounds(x, y, z, minimumX, minimumY, minimumZ, maximumX, maximumY, maximumZ))
    }

    @JvmStatic
    fun or(mainShape: VoxelShape, otherShape: VoxelShape): VoxelShape = join(mainShape, otherShape, BooleanOperator.OR)

    @JvmStatic
    fun or(mainShape: VoxelShape, vararg otherShapes: VoxelShape): VoxelShape = otherShapes.fold(mainShape, Shapes::or)

    @JvmStatic
    fun join(mainShape: VoxelShape, otherShape: VoxelShape, operator: BooleanOperator): VoxelShape =
        joinUnoptimized(mainShape, otherShape, operator).optimize()

    @JvmStatic
    fun joinUnoptimized(mainShape: VoxelShape, otherShape: VoxelShape, operator: BooleanOperator): VoxelShape {
        if (operator.apply(false, false)) throw IllegalArgumentException("Cannot use operator that evaluates to true with two false values!")
        if (mainShape === otherShape) {
            if (operator.apply(true, true)) return mainShape
            return empty()
        }
        val firstOnly = operator.apply(true, false)
        val secondOnly = operator.apply(false, true)
        if (mainShape.isEmpty()) {
            if (secondOnly) return otherShape
            return empty()
        }
        if (otherShape.isEmpty()) {
            if (firstOnly) return mainShape
            return empty()
        }
        val mergerX = createIndexMerger(1, mainShape.coordinates(Axis.X), otherShape.coordinates(Axis.X), firstOnly, secondOnly)
        val mergerY = createIndexMerger(mergerX.size - 1, mainShape.coordinates(Axis.Y), otherShape.coordinates(Axis.Y), firstOnly, secondOnly)
        val mergerZSize = (mergerX.size - 1) * (mergerY.size - 1)
        val mergerZ = createIndexMerger(mergerZSize, mainShape.coordinates(Axis.Z), otherShape.coordinates(Axis.Z), firstOnly, secondOnly)
        val shape = BitSetDiscreteVoxelShape.join(mainShape.shape, otherShape.shape, mergerX, mergerY, mergerZ, operator)
        if (mergerX is DiscreteCubeMerger && mergerY is DiscreteCubeMerger && mergerZ is DiscreteCubeMerger) return CubeVoxelShape(shape)
        return ArrayVoxelShape(shape, mergerX.list, mergerY.list, mergerZ.list)
    }

    @JvmStatic
    fun joinIsNotEmpty(mainShape: VoxelShape, otherShape: VoxelShape, operator: BooleanOperator): Boolean {
        require(!operator.apply(false, false)) { "Cannot use operator that evaluates to true with two false values!" }
        val mainEmpty = mainShape.isEmpty()
        val otherEmpty = otherShape.isEmpty()
        if (mainEmpty || otherEmpty) return operator.apply(!mainEmpty, !otherEmpty)
        if (mainShape === otherShape) return operator.apply(true, true)
        val firstOnly = operator.apply(true, false)
        val secondOnly = operator.apply(false, true)
        AxisCycle.AXIS_VALUES.forEach {
            if (mainShape.max(it) < otherShape.min(it) - EPSILON) return firstOnly || secondOnly
            if (otherShape.max(it) < mainShape.min(it) - EPSILON) return firstOnly || secondOnly
        }
        val mergerX = createIndexMerger(1, mainShape.coordinates(Axis.X), otherShape.coordinates(Axis.X), firstOnly, secondOnly)
        val mergerY = createIndexMerger(mergerX.size - 1, mainShape.coordinates(Axis.Y), otherShape.coordinates(Axis.Y), firstOnly, secondOnly)
        val mergerZSize = (mergerX.size - 1) * (mergerY.size - 1)
        val mergerZ = createIndexMerger(mergerZSize, mainShape.coordinates(Axis.Z), otherShape.coordinates(Axis.Z), firstOnly, secondOnly)
        return !mergerX.forMergedIndices { x1, x2, _ ->
            mergerY.forMergedIndices { y1, y2, _ ->
                mergerZ.forMergedIndices { z1, z2, _ ->
                    !operator.apply(mainShape.shape.isFullWide(x1, y1, z1), otherShape.shape.isFullWide(x2, y2, z2))
                }
            }
        }
    }

    @JvmStatic
    private fun findBits(minBits: Double, maxBits: Double): Int {
        @Suppress("SimplifyNegatedBinaryExpression") // Any of these could be NaN
        if (!(!(minBits < -EPSILON) && !(maxBits > 1.0 + EPSILON))) return -1
        for (i in 0..3) {
            val bitIndex = 1 shl i
            val minValue = minBits * bitIndex
            val maxValue = maxBits * bitIndex
            val minValueZero = abs(minValue - minValue.roundToInt()) < EPSILON * bitIndex
            val maxValueZero = abs(maxValue - maxValue.roundToInt()) < EPSILON * bitIndex
            if (minValueZero && maxValueZero) return i
        }
        return -1
    }

    @JvmStatic
    private fun createIndexMerger(size: Int, lower: DoubleList, upper: DoubleList, firstOnly: Boolean, secondOnly: Boolean): IndexMerger {
        val lowerMaxIndex = lower.size - 1
        val upperMaxIndex = upper.size - 1
        if (lower is CubePointRange && upper is CubePointRange) {
            val lcm = Maths.lcm(lowerMaxIndex, upperMaxIndex)
            if (size * lcm <= 256L) return DiscreteCubeMerger(lowerMaxIndex, upperMaxIndex)
        }
        if (lower.getDouble(lowerMaxIndex) < upper.getDouble(0) - EPSILON) return NonOverlappingMerger(lower, upper, false)
        if (upper.getDouble(upperMaxIndex) < lower.getDouble(0) - EPSILON) return NonOverlappingMerger(upper, lower, true)
        if (lowerMaxIndex == upperMaxIndex && Objects.equals(lower, upper)) return IdenticalMerger(lower)
        return IndirectMerger(lower, upper, firstOnly, secondOnly)
    }

    @JvmStatic
    private fun emptyList(): DoubleList = DoubleArrayList(doubleArrayOf(0.0))

    @JvmStatic
    private fun minMaxList(min: Double, max: Double): DoubleList = DoubleArrayList.wrap(doubleArrayOf(min, max))

    fun interface DoubleLineConsumer {

        fun consume(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double)
    }
}
