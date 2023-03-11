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
package org.kryptonmc.api.util

import org.kryptonmc.internal.annotations.ImmutableType
import kotlin.math.max

/**
 * An area in 3D space defined by two sets of points, a minimum and a maximum.
 *
 * This area forms a box, which can be used for collision detection. If a
 * bounding box intersects with something, we know that the two objects are
 * colliding.
 *
 * This is immutable, and as such, any method that would alter the state of the
 * bounding box returns a new instance with the requested changes.
 *
 * @property minX The minimum X value.
 * @property minY The minimum Y value.
 * @property minZ The minimum Z value.
 * @property maxX The maximum X value.
 * @property maxY The maximum Y value.
 * @property maxZ The maximum Z value.
 */
@JvmRecord
@ImmutableType
@Suppress("TooManyFunctions")
public data class BoundingBox(
    public val minX: Double,
    public val minY: Double,
    public val minZ: Double,
    public val maxX: Double,
    public val maxY: Double,
    public val maxZ: Double
) {

    init {
        require(minX <= maxX) { "Maximum X cannot be less than than minimum X!" }
        require(minY <= maxY) { "Maximum Y cannot be less than than minimum Y!" }
        require(minZ <= maxZ) { "Maximum Z cannot be less than than minimum Z!" }
    }

    /**
     * Creates a new bounding box with the given [min] and [max] values.
     *
     * @param min the minimum vector
     * @param max the maximum vector
     */
    public constructor(min: Vec3d, max: Vec3d) : this(min.x, min.y, min.z, max.x, max.y, max.z)

    /**
     * Creates a new bounding box with the given [min] and [max] values.
     *
     * @param min the minimum vector
     * @param max the maximum vector
     */
    public constructor(
        min: Vec3i,
        max: Vec3i
    ) : this(min.x.toDouble(), min.y.toDouble(), min.z.toDouble(), max.x.toDouble(), max.y.toDouble(), max.z.toDouble())

    /**
     * Gets the minimum vector of this bounding box.
     *
     * @return the minimum vector
     */
    public fun min(): Vec3d = Vec3d(minX, minY, minZ)

    /**
     * Gets the maximum vector of this bounding box.
     *
     * @return the maximum vector
     */
    public fun max(): Vec3d = Vec3d(maxX, maxY, maxZ)

    /**
     * Gets a vector representing the size of this bounding box on the X, Y,
     * and Z axes.
     *
     * @return the size of this bounding box
     */
    public fun size(): Vec3d = Vec3d(maxX - minX, maxY - minY, maxZ - minZ)

    /**
     * Calculates the total size of this bounding box.
     *
     * @return the total size
     */
    public fun totalSize(): Double = ((maxX - minX) + (maxY - minY) + (maxZ - minZ)) / 3.0

    /**
     * Gets a vector representing the center of this bounding box on the X, Y,
     * and Z axes.
     *
     * @return the center of this bounding box
     */
    public fun center(): Vec3d = Vec3d((minX + maxX) / 2, (minY + maxY) / 2, (minZ + maxZ) / 2)

    /**
     * Calculates the volume of this bounding box.
     *
     * @return the volume
     */
    public fun volume(): Double = (maxX - minX) * (maxY - minY) * (maxZ - minZ)

    /**
     * Gets the minimum value on the given [axis].
     *
     * @return the minimum value on the axis
     */
    public fun min(axis: Direction.Axis): Double = axis.select(minX, minY, minZ)

    /**
     * Gets the maximum value on the given [axis].
     *
     * @return the maximum value on the axis
     */
    public fun max(axis: Direction.Axis): Double = axis.select(minX, minY, minZ)

    /**
     * Inflates this bounding box by the given [x], [y], and [z] amounts and
     * returns the result.
     *
     * @param x the X amount to inflate by
     * @param y the Y amount to inflate by
     * @param z the Z amount to inflate by
     * @return the resulting box
     */
    public fun inflate(x: Double, y: Double, z: Double): BoundingBox {
        return BoundingBox(minX - x, minY - y, minZ - z, maxX + x, maxY + y, maxZ + z)
    }

    /**
     * Deflates this bounding box by the given [factor] and returns the
     * result.
     *
     * @param factor the factor to inflate by
     * @return the resulting box
     */
    public fun inflate(factor: Double): BoundingBox = inflate(factor, factor, factor)

    /**
     * Deflates this bounding box by the given [x], [y], and [z] amounts and
     * returns the result.
     *
     * @param x the X amount to deflate by
     * @param y the Y amount to deflate by
     * @param z the Z amount to deflate by
     * @return the resulting box
     */
    public fun deflate(x: Double, y: Double, z: Double): BoundingBox {
        return BoundingBox(minX + x, minY + y, minZ + z, maxX - x, maxY - y, maxZ - z)
    }

    /**
     * Deflates this bounding box by the given [factor] and returns the
     * result.
     *
     * @param factor the factor to deflate by
     * @return the resulting box
     */
    public fun deflate(factor: Double): BoundingBox = deflate(factor, factor, factor)

    /**
     * Checks if this bounding box intersects with the given [other] box.
     *
     * @param other the other bounding box
     * @return true if this box intersects with the other box
     */
    public fun intersects(other: BoundingBox): Boolean {
        return minX < other.maxX && maxX > other.minX && minY < other.maxY && maxY > other.minY && minZ < other.maxZ && maxZ > other.minZ
    }

    /**
     * Intersects this bounding box with the given [other] box and returns the
     * result.
     *
     * @param other the box to intersect with
     * @return the resulting box
     */
    public fun intersect(other: BoundingBox): BoundingBox {
        val newMinX = max(minX, other.minX)
        val newMinY = max(minY, other.minY)
        val newMinZ = max(minZ, other.minZ)
        val newMaxX = max(maxX, other.maxX)
        val newMaxY = max(maxY, other.maxY)
        val newMaxZ = max(maxZ, other.maxZ)
        return BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ)
    }

    /**
     * Checks if the given [x], [y], and [z] values are inside the bounds of
     * this box.
     *
     * @param x the X value
     * @param y the Y value
     * @param z the Z value
     * @return true if this box contains the values, false otherwise
     */
    public fun contains(x: Double, y: Double, z: Double): Boolean {
        return x >= minX && x < maxX && y >= minY && y < maxY && z >= minZ && z < maxZ
    }

    /**
     * Moves this bounding box by the given [x], [y], and [z] amounts and
     * returns the result.
     *
     * @param x the X amount
     * @param y the Y amount
     * @param z the Z amount
     * @return the resulting box
     */
    public fun move(x: Double, y: Double, z: Double): BoundingBox {
        return BoundingBox(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z)
    }

    /**
     * Moves this bounding box by the given [amount] and returns the result.
     *
     * @param amount the amount
     * @return the resulting box
     */
    public fun move(amount: Double): BoundingBox = move(amount, amount, amount)

    /**
     * Moves this bounding box by the given [amount] and returns the result.
     *
     * @param amount the amount
     * @return the resulting box
     */
    public fun move(amount: Vec3d): BoundingBox = move(amount.x, amount.y, amount.z)

    /**
     * Expands this bounding box by the given [x], [y], and [z] amounts and
     * returns the result.
     *
     * This differs from inflating in that it will only change either the
     * minimum or maximum value of any given axis, not both. An inflation
     * changes the area of the bounding box but does not change the center,
     * an expansion changes the area and the center.
     *
     * If any of the values are negative, the minimum value will be increased
     * on the respective axis, otherwise the maximum value will be increased.
     *
     * @param x the X amount to expand by
     * @param y the Y amount to expand by
     * @param z the Z amount to expand by
     * @return the resulting box
     */
    public fun expand(x: Double, y: Double, z: Double): BoundingBox {
        var minX = minX
        var minY = minY
        var minZ = minZ
        var maxX = maxX
        var maxY = maxY
        var maxZ = maxZ
        if (x < 0.0) minX += x else if (x > 0.0) maxX += x
        if (y < 0.0) minY += y else if (y > 0.0) maxY += y
        if (z < 0.0) minZ += z else if (z > 0.0) maxZ += z
        return BoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
    }

    /**
     * Expands this bounding box by the given [amount] and returns the result.
     *
     * @param amount the amount to expand by
     * @return the resulting box
     */
    public fun expand(amount: Double): BoundingBox = expand(amount, amount, amount)

    /**
     * Expands this bounding box by the given [amount] and returns the result.
     *
     * @param amount the amount to expand by
     * @return the resulting box
     */
    public fun expand(amount: Vec3d): BoundingBox = expand(amount.x, amount.y, amount.z)

    /**
     * Contracts this bounding box by the given [x], [y], and [z] amounts and
     * returns the result.
     *
     * This is the opposite of an expansion. If any of the values are negative,
     * the minimum value will be decreased on the respective axis, otherwise
     * the maximum value will be decreased.
     *
     * @param x the X amount to contract by
     * @param y the Y amount to contract by
     * @param z the Z amount to contract by
     * @return the resulting box
     */
    public fun contract(x: Double, y: Double, z: Double): BoundingBox {
        var minX = minX
        var minY = minY
        var minZ = minZ
        var maxX = maxX
        var maxY = maxY
        var maxZ = maxZ
        if (x < 0.0) minX -= x else if (x > 0.0) maxX -= x
        if (y < 0.0) minY -= y else if (y > 0.0) maxY -= y
        if (z < 0.0) minZ -= z else if (z > 0.0) maxZ -= z
        return BoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
    }

    /**
     * Contracts this bounding box by the given [amount] and returns the
     * result.
     *
     * @param amount the amount to contract by
     * @return the resulting box
     */
    public fun contract(amount: Double): BoundingBox = contract(amount, amount, amount)

    /**
     * Contracts this bounding box by the given [amount] and returns the
     * result.
     *
     * @param amount the amount to contract by
     * @return the resulting box
     */
    public fun contract(amount: Vec3d): BoundingBox = contract(amount.x, amount.y, amount.z)

    public companion object {

        /**
         * The bounding box that has a size of zero and is located at the
         * origin.
         */
        @JvmField
        public val ZERO: BoundingBox = BoundingBox(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    }
}
