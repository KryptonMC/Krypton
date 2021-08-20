/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import org.kryptonmc.api.space.BoundingBox
import org.kryptonmc.api.space.Vector

/**
 * Holder class for dimensions for an [Entity].
 *
 * @param width the width of the entity
 * @param height the height of the entity
 * @param isFixed if these dimensions are fixed
 */
class EntityDimensions(
    val width: Float,
    val height: Float,
    val isFixed: Boolean
) {

    /**
     * Creates a new bounding box from the given [center], using the [width]
     * and [height] dimensions to offset the [center] to obtain the minimum and
     * maximum values.
     *
     * @param center the centre position
     * @return a new bounding box from the centre coordinates
     */
    fun toBoundingBox(center: Vector): BoundingBox = toBoundingBox(center.x, center.y, center.z)

    /**
     * Creates a new bounding box from the given **centre** [x], [y], and [z]
     * coordinates, using the [width] and [height] dimensions to offset the centre
     * coordinates to obtain the minimum and maximum values.
     *
     * @param x the centre X coordinate
     * @param y the centre Y coordinate
     * @param z the centre Z coordinate
     * @return a new bounding box from the centre coordinates
     */
    fun toBoundingBox(x: Double, y: Double, z: Double): BoundingBox {
        val width = width / 2.0
        val height = height
        return BoundingBox(x - width, y, z - width, x + width, y + height, z + width)
    }

    /**
     * Scales the dimensions of this object by the given [factor], returning a new
     * object with the resulting changes if these dimensions are [not fixed][isFixed],
     * else returning this object and doing nothing if these dimensions [are fixed][isFixed],
     * or if the provided [factor] is equal to 1.
     *
     * @param factor the factor to scale the width and height by
     * @return see above
     */
    fun scale(factor: Float) = scale(factor, factor)

    /**
     * Scales the dimensions of this object by the given [factor], returning a new
     * object with the resulting changes if these dimensions are [not fixed][isFixed],
     * else returning this object and doing nothing if these dimensions [are fixed][isFixed],
     * or if the provided [factor] is equal to 1.
     *
     * @param factor the factor to scale the width and height by
     * @return see above
     */
    fun scale(factor: Double) = scale(factor.toFloat())

    /**
     * Scales the dimensions of this object by the given [factor], returning a new
     * object with the resulting changes if these dimensions are [not fixed][isFixed],
     * else returning this object and doing nothing if these dimensions [are fixed][isFixed],
     * or if the provided [factor] is equal to 1.
     *
     * @param factor the factor to scale the width and height by
     * @return see above
     */
    fun scale(factor: Int) = scale(factor.toFloat())

    /**
     * Scales the dimensions of this object by the given [factor], returning a new
     * object with the resulting changes if these dimensions are [not fixed][isFixed],
     * else returning this object and doing nothing if these dimensions [are fixed][isFixed],
     * or if the provided [factor] is equal to 1.
     *
     * @param factor the factor to scale the width and height by
     * @return see above
     */
    fun scale(factor: Long) = scale(factor.toFloat())

    /**
     * Scales the dimensions of this object by the given [width] and [height] factors,
     * returning a new object with the resulting changes if these dimensions are
     * [not fixed][isFixed], else returning this object and doing nothing if these
     * dimensions [are fixed][isFixed], or if the provided [width] and [height] values
     * are both equal to 1.
     *
     * @param width the width factor to scale the width by
     * @param height the height factor to scale the height by
     * @return see above
     */
    fun scale(width: Float, height: Float) = if (!isFixed && (width != 1F || height != 1F)) {
        scalable(this.width * width, this.height * height)
    } else this

    /**
     * Scales the dimensions of this object by the given [width] and [height] factors,
     * returning a new object with the resulting changes if these dimensions are
     * [not fixed][isFixed], else returning this object and doing nothing if these
     * dimensions [are fixed][isFixed], or if the provided [width] and [height] values
     * are both equal to 1.
     *
     * @param width the width factor to scale the width by
     * @param height the height factor to scale the height by
     * @return see above
     */
    fun scale(width: Double, height: Double) = scale(width.toFloat(), height.toFloat())

    /**
     * Scales the dimensions of this object by the given [width] and [height] factors,
     * returning a new object with the resulting changes if these dimensions are
     * [not fixed][isFixed], else returning this object and doing nothing if these
     * dimensions [are fixed][isFixed], or if the provided [width] and [height] values
     * are both equal to 1.
     *
     * @param width the width factor to scale the width by
     * @param height the height factor to scale the height by
     * @return see above
     */
    fun scale(width: Int, height: Int) = scale(width.toFloat(), height.toFloat())

    /**
     * Scales the dimensions of this object by the given [width] and [height] factors,
     * returning a new object with the resulting changes if these dimensions are
     * [not fixed][isFixed], else returning this object and doing nothing if these
     * dimensions [are fixed][isFixed], or if the provided [width] and [height] values
     * are both equal to 1.
     *
     * @param width the width factor to scale the width by
     * @param height the height factor to scale the height by
     * @return see above
     */
    fun scale(width: Long, height: Long) = scale(width.toFloat(), height.toFloat())

    override fun toString() = "EntityDimensions(width=$width, height=$height, isFixed=$isFixed)"

    companion object {

        /**
         * Creates new entity dimensions that can be scaled with [scale], with the
         * given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new scalable entity dimensions
         */
        @JvmStatic
        fun scalable(width: Float, height: Float) = EntityDimensions(width, height, false)

        /**
         * Creates new entity dimensions that can be scaled with [scale], with the
         * given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new scalable entity dimensions
         */
        @JvmStatic
        fun scalable(width: Double, height: Double) = scalable(width.toFloat(), height.toFloat())

        /**
         * Creates new entity dimensions that can be scaled with [scale], with the
         * given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new scalable entity dimensions
         */
        @JvmStatic
        fun scalable(width: Int, height: Int) = scalable(width.toFloat(), height.toFloat())

        /**
         * Creates new entity dimensions that can be scaled with [scale], with the
         * given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new scalable entity dimensions
         */
        @JvmStatic
        fun scalable(width: Long, height: Long) = scalable(width.toFloat(), height.toFloat())

        /**
         * Creates new entity dimensions that cannot scaled with [scale], with the
         * given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new fixed entity dimensions
         */
        @JvmStatic
        fun fixed(width: Float, height: Float) = EntityDimensions(width, height, true)

        /**
         * Creates new entity dimensions that cannot scaled with [scale], with the
         * given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new fixed entity dimensions
         */
        @JvmStatic
        fun fixed(width: Double, height: Double) = fixed(width.toFloat(), height.toFloat())

        /**
         * Creates new entity dimensions that cannot scaled with [scale], with the
         * given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new fixed entity dimensions
         */
        @JvmStatic
        fun fixed(width: Int, height: Int) = fixed(width.toFloat(), height.toFloat())

        /**
         * Creates new entity dimensions that cannot scaled with [scale], with the
         * given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new fixed entity dimensions
         */
        @JvmStatic
        fun fixed(width: Long, height: Long) = fixed(width.toFloat(), height.toFloat())
    }
}
