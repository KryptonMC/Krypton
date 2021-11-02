/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i

/**
 * A bounding box.
 *
 * These are immutable to guarantee thread-safe usage.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BoundingBox {

    /**
     * The minimum X value.
     */
    @get:JvmName("minimumX")
    public val minimumX: Double

    /**
     * The minimum Y value.
     */
    @get:JvmName("minimumY")
    public val minimumY: Double

    /**
     * The minimum Z value.
     */
    @get:JvmName("minimumZ")
    public val minimumZ: Double

    /**
     * The maximum X value.
     */
    @get:JvmName("maximumX")
    public val maximumX: Double

    /**
     * The maximum Y value.
     */
    @get:JvmName("maximumY")
    public val maximumY: Double

    /**
     * The maximum Z value.
     */
    @get:JvmName("maximumZ")
    public val maximumZ: Double

    /**
     * The size of this bounding box on the X axis.
     */
    @get:JvmName("xSize")
    public val xSize: Double

    /**
     * The size of this bounding box on the Y axis.
     */
    @get:JvmName("ySize")
    public val ySize: Double

    /**
     * The size of this bounding box on the Z axis.
     */
    @get:JvmName("zSize")
    public val zSize: Double

    /**
     * The total size of this bounding box.
     *
     * This is a mean average of the sizes on the 3 axes, calculated with the
     * formula:
     * `(xSize + ySize + zSize) / 3.0`
     */
    @get:JvmName("size")
    public val size: Double

    /**
     * The total volume of this bounding box.
     *
     * This is calculated by multiplying the 3 sizes together.
     */
    @get:JvmName("volume")
    public val volume: Double

    /**
     * The centre value on the X axis.
     *
     * This is calculated using a non-traditional method, in that the minimum
     * and maximum X value are linear interpolated with percentage 0.5.
     */
    @get:JvmName("centerX")
    public val centerX: Double

    /**
     * The centre value on the Y axis.
     *
     * This is calculated using a non-traditional method, in that the minimum
     * and maximum Y value are linear interpolated with percentage 0.5.
     */
    @get:JvmName("centerY")
    public val centerY: Double

    /**
     * The centre value on the Z axis.
     *
     * This is calculated using a non-traditional method, in that the minimum
     * and maximum Z value are linear interpolated with percentage 0.5.
     */
    @get:JvmName("centerZ")
    public val centerZ: Double

    /**
     * Gets the minimum value for the given [axis].
     *
     * @param axis the axis
     * @return the minimum value for the axis
     */
    public fun minimum(axis: Direction.Axis): Double

    /**
     * Gets the maximum value for the given [axis].
     *
     * @param axis the axis
     * @return the maximum value for the axis
     */
    public fun maximum(axis: Direction.Axis): Double

    /**
     * Inflates the border of this bounding box by the given [xFactor],
     * [yFactor], and [zFactor], and returns a new bounding box with the
     * applied changes.
     *
     * @param xFactor the X factor to inflate the border by
     * @param xFactor the Y factor to inflate the border by
     * @param xFactor the Z factor to inflate the border by
     * @return a new bounding box with its border inflated by the given factors
     */
    public fun inflate(xFactor: Double, yFactor: Double, zFactor: Double): BoundingBox

    /**
     * Inflates the border of this bounding box by the given [factor], and
     * returns a new bounding box with the applied changes.
     *
     * @param factor the factor to inflate the border by
     * @return a new bounding box with its border inflated by the given factor
     */
    public fun inflate(factor: Double): BoundingBox = inflate(factor, factor, factor)

    /**
     * Deflates the border of this bounding box by the given [xFactor],
     * [yFactor], and [zFactor], and returns a new bounding box with the
     * applied changes.
     *
     * This is equivalent to calling `inflate(-xFactor, -yFactor, -zFactor)`
     *
     * @param xFactor the X factor to deflate the border by
     * @param xFactor the Y factor to deflate the border by
     * @param xFactor the Z factor to deflate the border by
     * @return a new bounding box with its border deflated by the given factors
     */
    public fun deflate(xFactor: Double, yFactor: Double, zFactor: Double): BoundingBox = inflate(-xFactor, -yFactor, -zFactor)

    /**
     * Deflates the border of this bounding box by the given [factor], and
     * returns a new bounding box with the applied changes.
     *
     * This is equivalent to calling `inflate(-factor)`
     *
     * @param factor the factor to deflate the border by
     * @return a new bounding box with its border deflated by the given factor
     */
    public fun deflate(factor: Double): BoundingBox = inflate(-factor)

    /**
     * Intersects this bounding box with the given [other] bounding box, and
     * returns a new bounding box with the resulted intersection.
     *
     * @param other the other box to intersect with
     * @return a new bounding box with the resulting intersection
     */
    public fun intersect(other: BoundingBox): BoundingBox

    /**
     * Moves this bounding box by the specified [x], [y], and [z] amounts, and
     * returns a new bounding box with the result of the move.
     *
     * @param x the X amount
     * @param y the Y amount
     * @param z the Z amount
     * @return a new bounding box with the result of the move
     */
    public fun move(x: Double, y: Double, z: Double): BoundingBox

    /**
     * Moves this bounding box by the specified [amount], and returns a new
     * bounding box with the result of the move.
     *
     * @param amount the amount
     * @return a new bounding box with the result of the move
     */
    public fun move(amount: Vector3d): BoundingBox = move(amount.x(), amount.y(), amount.z())

    /**
     * Expands this bounding box out by the given [x], [y], and [z] amounts,
     * and returns a new bounding box with the result of the expansion.
     *
     * @param x the X amount
     * @param y the Y amount
     * @param z the Z amount
     * @return a new bounding box with the result of the expansion
     */
    public fun expand(x: Double, y: Double, z: Double): BoundingBox

    /**
     * Expands this bounding box out by the given [amount], and returns a new
     * bounding box with the result of the expansion.
     *
     * @param amount the amount
     * @return a new bounding box with the result of the expansion
     */
    public fun expand(amount: Vector3d): BoundingBox = expand(amount.x(), amount.y(), amount.z())

    /**
     * Contracts this bounding box by the given [x], [y], and [z] amounts, and
     * returns a new bounding box with the result of the contraction.
     *
     * @param x the X amount
     * @param y the Y amount
     * @param z the Z amount
     * @return a new bounding box with the result of the contraction
     */
    public fun contract(x: Double, y: Double, z: Double): BoundingBox

    /**
     * Returns true if this bounding box intersects with the given minimum and
     * maximum values, false otherwise.
     *
     * @param minimumX the minimum X value
     * @param minimumY the minimum Y value
     * @param minimumZ the minimum Z value
     * @param maximumX the maximum X value
     * @param maximumY the maximum Y value
     * @param maximumZ the maximum Z value
     * @return true if this box intersects with the values, false otherwise
     */
    public fun intersects(
        minimumX: Double,
        minimumY: Double,
        minimumZ: Double,
        maximumX: Double,
        maximumY: Double,
        maximumZ: Double
    ): Boolean

    /**
     * Returns true if this bounding box intersects with the given [other]
     * bounding box, false otherwise.
     *
     * @param other the other bounding box
     * @return true if this box intersects with the other box, false otherwise
     */
    public fun intersects(other: BoundingBox): Boolean

    /**
     * Returns true if the given [x], [y], and [z] values are inside the bounds
     * of this box, false otherwise.
     *
     * @param x the X value
     * @param y the Y value
     * @param z the Z value
     * @return true if this box contains the values, false otherwise
     */
    public fun contains(x: Double, y: Double, z: Double): Boolean

    /**
     * Returns true if the given [position] is inside the bounds of this box,
     * false otherwise.
     *
     * @param position the position
     * @return true if this box contains the position, false otherwise
     */
    public operator fun contains(position: Vector3d): Boolean = contains(position.x(), position.y(), position.z())

    @ApiStatus.Internal
    public interface Factory {

        public fun zero(): BoundingBox

        public fun unit(): BoundingBox

        public fun of(minimum: Vector3d, maximum: Vector3d): BoundingBox

        public fun of(minimum: Vector3i, maximum: Vector3i): BoundingBox

        public fun of(minimumX: Double, minimumY: Double, minimumZ: Double, maximumX: Double, maximumY: Double, maximumZ: Double): BoundingBox
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Gets the bounding box that has minimum and maximum values all set
         * to 0, meaning it is 0 in size on all axes, and has a volume and
         * centre of 0.
         */
        @JvmStatic
        public fun zero(): BoundingBox = FACTORY.zero()

        /**
         * Gets the bounding box that has minimum values set to 0, and maximum
         * values set to 1, meaning it is 1 in size on all axes, has a volume
         * of 1, and a centre of 0.5, 0.5, 0.5.
         */
        @JvmStatic
        public fun unit(): BoundingBox = FACTORY.unit()

        /**
         * Creates a new bounding box from the given values.
         *
         * The minimum values given here must all be less than their respective
         * maximum values.
         *
         * @param minimum the minimum vector
         * @param maximum the maximum vector
         * @return a new bounding box
         * @throws IllegalArgumentException if any of the minimum values are
         * greater than their respective maximum values, e.g. the maximum X is
         * greater than the minimum X
         */
        @JvmStatic
        public fun of(minimum: Vector3d, maximum: Vector3d): BoundingBox = FACTORY.of(minimum, maximum)

        /**
         * Creates a new bounding box from the given values.
         *
         * The minimum values given here must all be less than their respective
         * maximum values.
         *
         * @param minimum the minimum vector
         * @param maximum the maximum vector
         * @return a new bounding box
         * @throws IllegalArgumentException if any of the minimum values are
         * greater than their respective maximum values, e.g. the maximum X is
         * greater than the minimum X
         */
        @JvmStatic
        public fun of(minimum: Vector3i, maximum: Vector3i): BoundingBox = FACTORY.of(minimum, maximum)

        /**
         * Creates a new bounding box from the given values.
         *
         * The minimum values given here must all be less than their respective
         * maximum values.
         *
         * @param minimumX the minimum X value
         * @param minimumY the minimum Y value
         * @param minimumZ the minimum Z value
         * @param maximumX the maximum X value
         * @param maximumY the maximum Y value
         * @param maximumY the maximum Y value
         * @return a new bounding box
         * @throws IllegalArgumentException if any of the minimum values are
         * greater than their respective maximum values, e.g. the maximum X is
         * greater than the minimum X
         */
        @JvmStatic
        public fun of(
            minimumX: Double,
            minimumY: Double,
            minimumZ: Double,
            maximumX: Double,
            maximumY: Double,
            maximumZ: Double
        ): BoundingBox = FACTORY.of(minimumX, minimumY, minimumZ, maximumX, maximumY, maximumZ)
    }
}
