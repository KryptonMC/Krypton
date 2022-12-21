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
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.util.KryptonBoundingBox

class BoundingBoxVoxelShape(val box: BoundingBox) : VoxelShape(Shapes.unoptimizedBlock().shape) {

    private var cachedXs: DoubleList? = null
    private var cachedYs: DoubleList? = null
    private var cachedZs: DoubleList? = null

    override fun min(axis: Direction.Axis): Double = when (axis) {
        Direction.Axis.X -> box.minimumX
        Direction.Axis.Y -> box.minimumY
        Direction.Axis.Z -> box.minimumZ
    }

    override fun max(axis: Direction.Axis): Double = when (axis) {
        Direction.Axis.X -> box.maximumX
        Direction.Axis.Y -> box.maximumY
        Direction.Axis.Z -> box.maximumZ
    }

    override fun bounds(): BoundingBox = box

    override fun getCoordinates(axis: Direction.Axis): DoubleList = when (axis) {
        Direction.Axis.X -> {
            if (cachedXs == null) cachedXs = DoubleArrayList.wrap(doubleArrayOf(box.minimumX, box.maximumX))
            cachedXs!!
        }
        Direction.Axis.Y -> {
            if (cachedYs == null) cachedYs = DoubleArrayList.wrap(doubleArrayOf(box.minimumY, box.maximumY))
            cachedYs!!
        }
        Direction.Axis.Z -> {
            if (cachedZs == null) cachedZs = DoubleArrayList.wrap(doubleArrayOf(box.minimumZ, box.maximumZ))
            cachedZs!!
        }
    }

    @Suppress("MagicNumber")
    override fun get(axis: Direction.Axis, index: Int): Double = when (axis.ordinal or (index shl 2)) {
        0 or 0 shl 2 -> box.minimumX
        1 or (0 shl 2) -> box.minimumY
        2 or (0 shl 2) -> box.minimumZ
        0 or (1 shl 2) -> box.maximumX
        1 or (1 shl 2) -> box.maximumY
        2 or (1 shl 2) -> box.maximumZ
        else -> error("Unknown axis requested! Axis: $axis, Index: $index")
    }

    override fun isEmpty(): Boolean = Collisions.isEmpty(box)

    override fun intersects(box: BoundingBox): Boolean = Collisions.voxelShapeIntersect(this.box, box)

    override fun move(x: Double, y: Double, z: Double): VoxelShape = BoundingBoxVoxelShape(box.move(x, y, z))

    override fun optimize(): VoxelShape {
        if (isEmpty()) return Shapes.empty()
        if (this === Shapes.OPTIMIZED_BLOCK || box == Shapes.OPTIMIZED_BLOCK.box) return Shapes.OPTIMIZED_BLOCK
        return this
    }

    override fun forAllBoxes(consumer: Shapes.DoubleLineConsumer) {
        consumer.consume(box.minimumX, box.minimumY, box.minimumZ, box.maximumX, box.maximumY, box.maximumZ)
    }

    override fun toBoundingBoxes(): List<BoundingBox> = listOf(box)

    override fun findIndex(axis: Direction.Axis, position: Double): Int = when (axis) {
        Direction.Axis.X -> findIndex(position, BoundingBox::minimumX, BoundingBox::maximumX)
        Direction.Axis.Y -> findIndex(position, BoundingBox::minimumY, BoundingBox::maximumY)
        Direction.Axis.Z -> findIndex(position, BoundingBox::minimumZ, BoundingBox::maximumZ)
    }

    override fun calculateFace(direction: Direction): VoxelShape {
        if (isEmpty()) return Shapes.empty()
        if (this === Shapes.OPTIMIZED_BLOCK) return this
        when (direction) {
            Direction.EAST, Direction.WEST -> { // +X, -X
                val from = if (direction == Direction.EAST) 1.0 - Collisions.EPSILON else Collisions.EPSILON
                if (from > box.maximumX || box.minimumX > from) return Shapes.empty()
                return BoundingBoxVoxelShape(KryptonBoundingBox(0.0, box.minimumY, box.minimumZ, 1.0, box.maximumY, box.maximumZ)).optimize()
            }
            Direction.UP, Direction.DOWN -> { // +Y, -Y
                val from = if (direction == Direction.UP) 1.0 - Collisions.EPSILON else Collisions.EPSILON
                if (from > box.maximumY || box.minimumY > from) return Shapes.empty()
                return BoundingBoxVoxelShape(KryptonBoundingBox(box.minimumX, 0.0, box.minimumZ, box.maximumX, 1.0, box.maximumZ)).optimize()
            }
            Direction.SOUTH, Direction.NORTH -> {
                val from = if (direction == Direction.SOUTH) 1.0 - Collisions.EPSILON else Collisions.EPSILON
                if (from > box.maximumZ || box.minimumZ > from) return Shapes.empty()
                return BoundingBoxVoxelShape(KryptonBoundingBox(box.minimumX, box.minimumY, 0.0, box.maximumX, box.maximumY, 1.0)).optimize()
            }
        }
    }

    override fun collide(axis: Direction.Axis, box: BoundingBox, maxDistance: Double): Double {
        if (Collisions.isEmpty(this.box) || Collisions.isEmpty(box)) return maxDistance
        return when (axis) {
            Direction.Axis.X -> Collisions.collideX(this.box, box, maxDistance)
            Direction.Axis.Y -> Collisions.collideY(this.box, box, maxDistance)
            Direction.Axis.Z -> Collisions.collideZ(this.box, box, maxDistance)
        }
    }

    private inline fun findIndex(position: Double, minGetter: (BoundingBox) -> Double, maxGetter: (BoundingBox) -> Double): Int {
        if (position < maxGetter(box)) {
            if (position < minGetter(box)) return -1
            return 0
        }
        return 1
    }
}
