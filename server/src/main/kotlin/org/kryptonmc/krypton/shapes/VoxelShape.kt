/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.shapes

import com.google.common.math.DoubleMath
import it.unimi.dsi.fastutil.doubles.DoubleList
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Direction.Axis
import org.kryptonmc.krypton.shapes.discrete.DiscreteVoxelShape
import org.kryptonmc.krypton.shapes.util.BooleanOperator
import org.kryptonmc.krypton.shapes.util.OffsetDoubleList
import org.kryptonmc.krypton.util.math.Maths
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
        return BoundingBox(min(Axis.X), min(Axis.Y), min(Axis.Z), max(Axis.X), max(Axis.Y), max(Axis.Z))
    }

    abstract fun getCoordinates(axis: Axis): DoubleList

    protected open fun get(axis: Axis, index: Int): Double = getCoordinates(axis).getDouble(index)

    open fun isEmpty(): Boolean = shape.isEmpty()

    open fun intersects(box: BoundingBox): Boolean = Shapes.joinIsNotEmpty(this, BoundingBoxVoxelShape(box), BooleanOperator.AND)

    open fun move(x: Double, y: Double, z: Double): VoxelShape {
        if (isEmpty()) return Shapes.empty()
        val offsetXs = OffsetDoubleList(getCoordinates(Axis.X), x)
        val offsetYs = OffsetDoubleList(getCoordinates(Axis.Y), y)
        val offsetZs = OffsetDoubleList(getCoordinates(Axis.Z), z)
        return ArrayVoxelShape(shape, offsetXs, offsetYs, offsetZs)
    }

    open fun optimize(): VoxelShape {
        var shape = Shapes.empty()
        forAllBoxes { x1, y1, z1, x2, y2, z2 -> shape = Shapes.joinUnoptimized(shape, Shapes.box(x1, y1, z1, x2, y2, z2), BooleanOperator.OR) }
        return shape
    }

    open fun forAllBoxes(consumer: Shapes.DoubleLineConsumer) {
        val xs = getCoordinates(Axis.X)
        val ys = getCoordinates(Axis.Y)
        val zs = getCoordinates(Axis.Z)
        shape.forAllBoxes(true) { x1, y1, z1, x2, y2, z2 ->
            consumer.consume(xs.getDouble(x1), ys.getDouble(y1), zs.getDouble(z1), xs.getDouble(x2), ys.getDouble(y2), zs.getDouble(z2))
        }
    }

    open fun toBoundingBoxes(): List<BoundingBox> {
        val boxes = persistentListOf<BoundingBox>().builder()
        forAllBoxes { x1, y1, z1, x2, y2, z2 -> boxes.add(BoundingBox(x1, y1, z1, x2, y2, z2)) }
        return boxes.build()
    }

    fun min(axis: Axis, primaryPosition: Double, secondaryPosition: Double): Double =
        minOrMax(axis, primaryPosition, secondaryPosition, shape::firstFull) {
            if (it >= shape.size(axis)) Double.POSITIVE_INFINITY else get(axis, it)
        }

    fun max(axis: Axis, primaryPosition: Double, secondaryPosition: Double): Double =
        minOrMax(axis, primaryPosition, secondaryPosition, shape::lastFull) { if (it <= 0) Double.NEGATIVE_INFINITY else get(axis, it) }

    private inline fun minOrMax(axis: Axis, primaryPosition: Double, secondaryPosition: Double, fullProvider: (Axis, Int, Int) -> Int,
                                checker: (Int) -> Double): Double {
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
            faces = arrayOfNulls(FACES)
        }
        val face = calculateFace(direction)
        faces!![direction.ordinal] = face
        return face
    }

    protected open fun calculateFace(direction: Direction): VoxelShape {
        val coordinates = getCoordinates(direction.axis)
        val firstIsZero = DoubleMath.fuzzyEquals(coordinates.getDouble(0), 0.0, Shapes.EPSILON)
        val secondIsOne = DoubleMath.fuzzyEquals(coordinates.getDouble(1), 1.0, Shapes.EPSILON)
        if (coordinates.size == 2 && firstIsZero && secondIsOne) return this
        val position = if (direction.axisDirection == Direction.AxisDirection.POSITIVE) 1.0 - Shapes.EPSILON else Shapes.EPSILON
        return SliceShape(this, direction.axis, findIndex(direction.axis, position))
    }

    open fun collide(axis: Axis, box: BoundingBox, maxDistance: Double): Double {
        return collideX(AxisCycle.between(axis, Axis.X), box, maxDistance)
    }

    private fun collideX(cycle: AxisCycle, box: BoundingBox, maxDistance: Double): Double {
        if (isEmpty()) return maxDistance
        if (abs(maxDistance) < Shapes.EPSILON) return 0.0

        val inverse = cycle.inverse()
        val axisX = cycle.cycle(Axis.X)
        val axisY = cycle.cycle(Axis.Y)
        val axisZ = cycle.cycle(Axis.Z)
        val minIndexY = kotlin.math.max(0, findIndex(axisY, box.min(axisY) + Shapes.EPSILON))
        val maxIndexY = kotlin.math.min(shape.size(axisY), findIndex(axisY, box.max(axisY) - Shapes.EPSILON) + 1)
        val minIndexZ = kotlin.math.max(0, findIndex(axisZ, box.min(axisZ) + Shapes.EPSILON))
        val maxIndexZ = kotlin.math.min(shape.size(axisZ), findIndex(axisZ, box.max(axisZ) - Shapes.EPSILON) + 1)

        var tempMaxDistance = maxDistance
        if (tempMaxDistance > 0.0) {
            val maxX = box.max(axisX)
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
            val minX = box.min(axisX)
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

    companion object {

        private const val FACES = 6
    }
}
