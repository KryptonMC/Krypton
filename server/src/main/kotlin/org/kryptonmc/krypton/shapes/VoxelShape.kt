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

import com.google.common.math.DoubleMath
import it.unimi.dsi.fastutil.doubles.DoubleList
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.util.KryptonBoundingBox
import org.kryptonmc.krypton.util.Maths

abstract class VoxelShape(val shape: DiscreteVoxelShape) {

    private var faces: Array<VoxelShape?>? = null
    private var cachedBoundingBoxes: List<BoundingBox>? = null

    fun min(axis: Direction.Axis): Double {
        val firstFull = shape.firstFull(axis)
        return if (firstFull >= shape.size(axis)) Double.POSITIVE_INFINITY else get(axis, firstFull)
    }

    fun max(axis: Direction.Axis): Double {
        val lastFull = shape.lastFull(axis)
        return if (lastFull <= 0) Double.NEGATIVE_INFINITY else get(axis, lastFull)
    }

    abstract fun coordinates(axis: Direction.Axis): DoubleList

    protected fun get(axis: Direction.Axis, index: Int): Double = coordinates(axis).getDouble(index)

    fun isEmpty(): Boolean = shape.isEmpty()

    fun optimize(): VoxelShape {
        var shape = Shapes.empty()
        forAllBoxes { x1, y1, z1, x2, y2, z2 -> shape = Shapes.joinUnoptimized(shape, Shapes.box(x1, y1, z1, x2, y2, z2), BooleanOperator.OR) }
        return shape
    }

    fun forAllBoxes(consumer: Shapes.DoubleLineConsumer) {
        val xs = coordinates(Direction.Axis.X)
        val ys = coordinates(Direction.Axis.Y)
        val zs = coordinates(Direction.Axis.Z)
        shape.forAllBoxes(true) { x1, y1, z1, x2, y2, z2 ->
            consumer.consume(xs.getDouble(x1), ys.getDouble(y1), zs.getDouble(z1), xs.getDouble(x2), ys.getDouble(y2), zs.getDouble(z2))
        }
    }

    fun toBoundingBoxes(): List<BoundingBox> {
        if (cachedBoundingBoxes != null) return cachedBoundingBoxes!!
        val builder = persistentListOf<BoundingBox>().builder()
        forAllBoxes { x1, y1, z1, x2, y2, z2 -> builder.add(KryptonBoundingBox(x1, y1, z1, x2, y2, z2)) }
        val boxes = builder.build()
        cachedBoundingBoxes = boxes
        return boxes
    }

    fun getFaceShape(direction: Direction): VoxelShape {
        if (isEmpty() || this === Shapes.block()) return this
        if (faces != null) {
            val face = faces!![direction.ordinal]
            if (face != null) return face
        } else {
            faces = arrayOfNulls(6)
        }
        val face = calculateFace(direction)
        faces!![direction.ordinal] = face
        return face
    }

    private fun calculateFace(direction: Direction): VoxelShape {
        val coordinates = coordinates(direction.axis)
        val firstIsZero = DoubleMath.fuzzyEquals(coordinates.getDouble(0), 0.0, Shapes.EPSILON)
        val secondIsOne = DoubleMath.fuzzyEquals(coordinates.getDouble(1), 1.0, Shapes.EPSILON)
        if (coordinates.size == 2 && firstIsZero && secondIsOne) return this
        val position = if (direction.axisDirection == Direction.AxisDirection.POSITIVE) 1.0 - Shapes.EPSILON else Shapes.EPSILON
        return SliceShape(this, direction.axis, findIndex(direction.axis, position))
    }

    fun min(axis: Direction.Axis, primaryPosition: Double, secondaryPosition: Double): Double =
        minOrMax(axis, primaryPosition, secondaryPosition, shape::firstFull) {
            if (it >= shape.size(axis)) Double.POSITIVE_INFINITY else get(axis, it)
        }

    fun max(axis: Direction.Axis, primaryPosition: Double, secondaryPosition: Double): Double =
        minOrMax(axis, primaryPosition, secondaryPosition, shape::lastFull) { if (it <= 0) Double.NEGATIVE_INFINITY else get(axis, it) }

    private inline fun minOrMax(
        axis: Direction.Axis,
        primaryPosition: Double,
        secondaryPosition: Double,
        fullProvider: (Direction.Axis, Int, Int) -> Int,
        checker: (Int) -> Double
    ): Double {
        val forward = AxisCycle.FORWARD.cycle(axis)
        val backward = AxisCycle.BACKWARD.cycle(axis)
        val forwardIndex = findIndex(forward, primaryPosition)
        val backwardIndex = findIndex(backward, secondaryPosition)
        return checker(fullProvider(axis, forwardIndex, backwardIndex))
    }

    protected open fun findIndex(axis: Direction.Axis, position: Double): Int =
        Maths.fastBinarySearch(0, shape.size(axis) + 1) { position < get(axis, it) } - 1
}
