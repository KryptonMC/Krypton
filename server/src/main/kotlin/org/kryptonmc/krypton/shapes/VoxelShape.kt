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
import org.kryptonmc.api.util.Direction.Axis
import org.kryptonmc.krypton.util.KryptonBoundingBox
import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.util.math.AxisCycle
import kotlin.math.abs

/**
 * The shape of a voxel object. This is used for block shapes, as blocks are
 * all voxels, uniform 1 metre x 1 metre x 1 metre cubes in a locked grid.
 *
 * This shape also handles other shapes on different faces, as the shape of a
 * block may vary depending on the face it is viewed from. For example, the
 * shape of a cauldron from the bottom or any of the sides is a regular cube,
 * but the face on the top is a regular cube minus a smaller inside cube.
 */
abstract class VoxelShape(val shape: DiscreteVoxelShape) {

    // The shapes on different faces of the voxel.
    private var faces: Array<VoxelShape?>? = null

    open fun min(axis: Axis): Double {
        val firstFull = shape.firstFull(axis)
        return if (firstFull >= shape.size(axis)) Double.POSITIVE_INFINITY else get(axis, firstFull)
    }

    open fun max(axis: Axis): Double {
        val lastFull = shape.lastFull(axis)
        return if (lastFull <= 0) Double.NEGATIVE_INFINITY else get(axis, lastFull)
    }

    open fun bounds(): BoundingBox {
        if (isEmpty()) throw UnsupportedOperationException("Empty shapes do not have bounds! They are empty!")
        return KryptonBoundingBox(min(Axis.X), min(Axis.Y), min(Axis.Z), max(Axis.X), max(Axis.Y), max(Axis.Z))
    }

    abstract fun coordinates(axis: Axis): DoubleList

    protected open fun get(axis: Axis, index: Int): Double = coordinates(axis).getDouble(index)

    open fun isEmpty(): Boolean = shape.isEmpty()

    open fun intersects(box: BoundingBox): Boolean = Shapes.joinIsNotEmpty(this, BoundingBoxVoxelShape(box), BooleanOperator.AND)

    open fun move(x: Double, y: Double, z: Double): VoxelShape {
        if (isEmpty()) return Shapes.empty()
        val offsetXs = OffsetDoubleList(coordinates(Axis.X), x)
        val offsetYs = OffsetDoubleList(coordinates(Axis.Y), y)
        val offsetZs = OffsetDoubleList(coordinates(Axis.Z), z)
        return ArrayVoxelShape(shape, offsetXs, offsetYs, offsetZs)
    }

    open fun optimize(): VoxelShape {
        var shape = Shapes.empty()
        forAllBoxes { x1, y1, z1, x2, y2, z2 -> shape = Shapes.joinUnoptimized(shape, Shapes.box(x1, y1, z1, x2, y2, z2), BooleanOperator.OR) }
        return shape
    }

    open fun forAllBoxes(consumer: Shapes.DoubleLineConsumer) {
        val xs = coordinates(Axis.X)
        val ys = coordinates(Axis.Y)
        val zs = coordinates(Axis.Z)
        shape.forAllBoxes(true) { x1, y1, z1, x2, y2, z2 ->
            consumer.consume(xs.getDouble(x1), ys.getDouble(y1), zs.getDouble(z1), xs.getDouble(x2), ys.getDouble(y2), zs.getDouble(z2))
        }
    }

    open fun toBoundingBoxes(): List<BoundingBox> {
        val boxes = persistentListOf<BoundingBox>().builder()
        forAllBoxes { x1, y1, z1, x2, y2, z2 -> boxes.add(KryptonBoundingBox(x1, y1, z1, x2, y2, z2)) }
        return boxes.build()
    }

    fun min(axis: Axis, primaryPosition: Double, secondaryPosition: Double): Double =
        minOrMax(axis, primaryPosition, secondaryPosition, shape::firstFull) {
            if (it >= shape.size(axis)) Double.POSITIVE_INFINITY else get(axis, it)
        }

    fun max(axis: Axis, primaryPosition: Double, secondaryPosition: Double): Double =
        minOrMax(axis, primaryPosition, secondaryPosition, shape::lastFull) { if (it <= 0) Double.NEGATIVE_INFINITY else get(axis, it) }

    private inline fun minOrMax(
        axis: Axis,
        primaryPosition: Double,
        secondaryPosition: Double,
        fullProvider: (Axis, Int, Int) -> Int,
        checker: (Int) -> Double
    ): Double {
        val forward = AxisCycle.FORWARD.cycle(axis)
        val backward = AxisCycle.BACKWARD.cycle(axis)
        val forwardIndex = findIndex(forward, primaryPosition)
        val backwardIndex = findIndex(backward, secondaryPosition)
        return checker(fullProvider(axis, forwardIndex, backwardIndex))
    }

    protected open fun findIndex(axis: Axis, position: Double): Int =
        Maths.fastBinarySearch(0, shape.size(axis) + 1) { position < get(axis, it) } - 1

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

    protected open fun calculateFace(direction: Direction): VoxelShape {
        val coordinates = coordinates(direction.axis)
        val firstIsZero = DoubleMath.fuzzyEquals(coordinates.getDouble(0), 0.0, Shapes.EPSILON)
        val secondIsOne = DoubleMath.fuzzyEquals(coordinates.getDouble(1), 1.0, Shapes.EPSILON)
        if (coordinates.size == 2 && firstIsZero && secondIsOne) return this
        val position = if (direction.axisDirection == Direction.AxisDirection.POSITIVE) 1.0 - Shapes.EPSILON else Shapes.EPSILON
        return SliceShape(this, direction.axis, findIndex(direction.axis, position))
    }

    open fun collide(axis: Axis, box: BoundingBox, maxDistance: Double): Double = collideX(AxisCycle.between(axis, Axis.X), box, maxDistance)

    private fun collideX(cycle: AxisCycle, box: BoundingBox, maxDistance: Double): Double {
        if (isEmpty()) return maxDistance
        if (abs(maxDistance) < Shapes.EPSILON) return 0.0
        val inverse = cycle.inverse()
        val axisX = cycle.cycle(Axis.X)
        val axisY = cycle.cycle(Axis.Y)
        val axisZ = cycle.cycle(Axis.Z)
        val minIndexY = kotlin.math.max(0, findIndex(axisY, box.minimum(axisY) + Shapes.EPSILON))
        val maxIndexY = kotlin.math.min(shape.size(axisY), findIndex(axisY, box.maximum(axisY) - Shapes.EPSILON) + 1)
        val minIndexZ = kotlin.math.max(0, findIndex(axisZ, box.minimum(axisZ) + Shapes.EPSILON))
        val maxIndexZ = kotlin.math.min(shape.size(axisZ), findIndex(axisZ, box.maximum(axisZ) - Shapes.EPSILON) + 1)
        var tempMaxDistance = maxDistance
        if (tempMaxDistance > 0.0) {
            val maxX = box.maximum(axisX)
            for (x in findIndex(axisX, maxX - Shapes.EPSILON) + 1 until shape.size(axisX)) {
                for (y in minIndexY until maxIndexY) {
                    for (z in minIndexZ until maxIndexZ) {
                        if (!shape.isFullWide(inverse, x, y, z)) continue
                        val value = get(axisX, x) - maxX
                        if (value >= -Shapes.EPSILON) tempMaxDistance = kotlin.math.min(tempMaxDistance, value)
                        return tempMaxDistance
                    }
                }
            }
        } else if (tempMaxDistance < 0.0) {
            val minX = box.minimum(axisX)
            for (x in findIndex(axisX, minX + Shapes.EPSILON) - 1 downTo 0) {
                for (y in minIndexY until maxIndexY) {
                    for (z in minIndexZ until maxIndexZ) {
                        if (!shape.isFullWide(inverse, x, y, z)) continue
                        val value = get(axisX, x + 1) - minX
                        if (value <= Shapes.EPSILON) tempMaxDistance = kotlin.math.max(tempMaxDistance, value)
                        return tempMaxDistance
                    }
                }
            }
        }
        return tempMaxDistance
    }
}
