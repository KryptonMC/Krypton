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
import org.kryptonmc.krypton.shapes.collision.Collisions

class BoundingBoxVoxelShape(val box: BoundingBox) : VoxelShape(Shapes.unoptimizedBlock().shape) {

    private var cachedXs: DoubleList? = null
    private var cachedYs: DoubleList? = null
    private var cachedZs: DoubleList? = null

    override fun min(axis: Direction.Axis): Double = box.min(axis)

    override fun max(axis: Direction.Axis): Double = box.max(axis)

    override fun bounds(): BoundingBox = box

    override fun getCoordinates(axis: Direction.Axis): DoubleList = when (axis) {
        Direction.Axis.X -> {
            if (cachedXs == null) cachedXs = DoubleArrayList.wrap(doubleArrayOf(box.minX, box.maxX))
            cachedXs!!
        }
        Direction.Axis.Y -> {
            if (cachedYs == null) cachedYs = DoubleArrayList.wrap(doubleArrayOf(box.minY, box.maxY))
            cachedYs!!
        }
        Direction.Axis.Z -> {
            if (cachedZs == null) cachedZs = DoubleArrayList.wrap(doubleArrayOf(box.minZ, box.maxZ))
            cachedZs!!
        }
    }

    @Suppress("MagicNumber")
    override fun get(axis: Direction.Axis, index: Int): Double = when (axis.ordinal or (index shl 2)) {
        0 or 0 shl 2 -> box.minX
        1 or (0 shl 2) -> box.minY
        2 or (0 shl 2) -> box.minZ
        0 or (1 shl 2) -> box.maxX
        1 or (1 shl 2) -> box.maxY
        2 or (1 shl 2) -> box.maxZ
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
        consumer.consume(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)
    }

    override fun toBoundingBoxes(): List<BoundingBox> = listOf(box)

    override fun findIndex(axis: Direction.Axis, position: Double): Int = when (axis) {
        Direction.Axis.X -> findIndex(position, BoundingBox::minX, BoundingBox::maxX)
        Direction.Axis.Y -> findIndex(position, BoundingBox::minY, BoundingBox::maxY)
        Direction.Axis.Z -> findIndex(position, BoundingBox::minZ, BoundingBox::maxZ)
    }

    override fun calculateFace(direction: Direction): VoxelShape {
        if (isEmpty()) return Shapes.empty()
        if (this === Shapes.OPTIMIZED_BLOCK) return this
        when (direction) {
            Direction.EAST, Direction.WEST -> { // +X, -X
                val from = if (direction == Direction.EAST) 1.0 - Collisions.EPSILON else Collisions.EPSILON
                if (from > box.maxX || box.minX > from) return Shapes.empty()
                return BoundingBoxVoxelShape(BoundingBox(0.0, box.minY, box.minZ, 1.0, box.maxY, box.maxZ)).optimize()
            }
            Direction.UP, Direction.DOWN -> { // +Y, -Y
                val from = if (direction == Direction.UP) 1.0 - Collisions.EPSILON else Collisions.EPSILON
                if (from > box.maxY || box.minY > from) return Shapes.empty()
                return BoundingBoxVoxelShape(BoundingBox(box.minX, 0.0, box.minZ, box.maxX, 1.0, box.maxZ)).optimize()
            }
            Direction.SOUTH, Direction.NORTH -> {
                val from = if (direction == Direction.SOUTH) 1.0 - Collisions.EPSILON else Collisions.EPSILON
                if (from > box.maxZ || box.minZ > from) return Shapes.empty()
                return BoundingBoxVoxelShape(BoundingBox(box.minX, box.minY, 0.0, box.maxX, box.maxY, 1.0)).optimize()
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
