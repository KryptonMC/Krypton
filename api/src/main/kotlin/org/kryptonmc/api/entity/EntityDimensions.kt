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
public data class EntityDimensions(
    public val width: Float,
    public val height: Float,
    public val isFixed: Boolean
) {

    /**
     * Creates a new bounding box from the given [center], using the [width]
     * and [height] dimensions to offset the [center] to obtain the minimum and
     * maximum values.
     *
     * @param center the centre position
     * @return a new bounding box from the centre coordinates
     */
    public fun toBoundingBox(center: Vector): BoundingBox = toBoundingBox(center.x, center.y, center.z)

    /**
     * Creates a new bounding box from the given **centre** [x], [y], and [z]
     * coordinates, using the [width] and [height] dimensions to offset the
     * centre coordinates to obtain the minimum and maximum values.
     *
     * @param x the centre X coordinate
     * @param y the centre Y coordinate
     * @param z the centre Z coordinate
     * @return a new bounding box from the centre coordinates
     */
    public fun toBoundingBox(x: Double, y: Double, z: Double): BoundingBox {
        val width = width / 2.0
        val height = height
        return BoundingBox(
            x - width,
            y,
            z - width,
            x + width,
            y + height,
            z + width
        )
    }

    /**
     * Scales the dimensions of this object by the given [factor], returning a
     * new object with the resulting changes if these dimensions are
     * [not fixed][isFixed], else returning this object and doing nothing if
     * these dimensions [are fixed][isFixed], or if the provided [factor] is 1.
     *
     * @param factor the factor to scale the width and height by
     * @return see above
     */
    public fun scale(factor: Float): EntityDimensions = scale(factor, factor)

    /**
     * Scales the dimensions of this object by the given [factor], returning a
     * new object with the resulting changes if these dimensions are
     * [not fixed][isFixed], else returning this object and doing nothing if
     * these dimensions [are fixed][isFixed], or if the provided [factor] is 1.
     *
     * @param factor the factor to scale the width and height by
     * @return see above
     */
    public fun scale(factor: Double): EntityDimensions = scale(factor.toFloat())

    /**
     * Scales the dimensions of this object by the given [factor], returning a
     * new object with the resulting changes if these dimensions are
     * [not fixed][isFixed] else returning this object and doing nothing if
     * these dimensions [are fixed][isFixed], or if the provided [factor] is 1.
     *
     * @param factor the factor to scale the width and height by
     * @return see above
     */
    public fun scale(factor: Int): EntityDimensions = scale(factor.toFloat())

    /**
     * Scales the dimensions of this object by the given [factor], returning a
     * new object with the resulting changes if these dimensions are
     * [not fixed][isFixed], else returning this object and doing nothing if
     * these dimensions [are fixed][isFixed], or if the provided [factor] is 1.
     *
     * @param factor the factor to scale the width and height by
     * @return see above
     */
    public fun scale(factor: Long): EntityDimensions = scale(factor.toFloat())

    /**
     * Scales the dimensions of this object by the given [width] and [height]
     * factors, returning a new object with the resulting changes if these
     * dimensions are [not fixed][isFixed], else returning this object and
     * doing nothing if these dimensions [are fixed][isFixed], or if the
     * provided [width] and [height] values are both 1.
     *
     * @param width the width factor to scale the width by
     * @param height the height factor to scale the height by
     * @return see above
     */
    public fun scale(width: Float, height: Float): EntityDimensions {
        if (isFixed || width == 1F && height == 1F) return this
        return scalable(this.width * width, this.height * height)
    }

    /**
     * Scales the dimensions of this object by the given [width] and [height]
     * factors, returning a new object with the resulting changes if these
     * dimensions are [not fixed][isFixed], else returning this object and
     * doing nothing if these dimensions [are fixed][isFixed], or if the
     * provided [width] and [height] value are both 1.
     *
     * @param width the width factor to scale the width by
     * @param height the height factor to scale the height by
     * @return see above
     */
    public fun scale(width: Double, height: Double): EntityDimensions = scale(width.toFloat(), height.toFloat())

    /**
     * Scales the dimensions of this object by the given [width] and [height]
     * factors, returning a new object with the resulting changes if these
     * dimensions are [not fixed][isFixed], else returning this object and
     * doing nothing if these dimensions [are fixed][isFixed], or if the
     * provided [width] and [height] values are both 1.
     *
     * @param width the width factor to scale the width by
     * @param height the height factor to scale the height by
     * @return see above
     */
    public fun scale(width: Int, height: Int): EntityDimensions = scale(width.toFloat(), height.toFloat())

    /**
     * Scales the dimensions of this object by the given [width] and [height]
     * factors, returning a new object with the resulting changes if these
     * dimensions are [not fixed][isFixed], else returning this object and
     * doing nothing if these dimensions [are fixed][isFixed], or if the
     * provided [width] and [height] values are both 1.
     *
     * @param width the width factor to scale the width by
     * @param height the height factor to scale the height by
     * @return see above
     */
    public fun scale(width: Long, height: Long): EntityDimensions = scale(width.toFloat(), height.toFloat())

    override fun toString(): String = "EntityDimensions(width=$width, height=$height, isFixed=$isFixed)"

    public companion object {

        /**
         * Creates new entity dimensions that can be scaled with [scale], with
         * the given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new scalable entity dimensions
         */
        @JvmStatic
        public fun scalable(width: Float, height: Float): EntityDimensions =
            EntityDimensions(width, height, false)

        /**
         * Creates new entity dimensions that can be scaled with [scale], with
         * the given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new scalable entity dimensions
         */
        @JvmStatic
        public fun scalable(width: Double, height: Double): EntityDimensions =
            scalable(width.toFloat(), height.toFloat())

        /**
         * Creates new entity dimensions that can be scaled with [scale], with
         * the given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new scalable entity dimensions
         */
        @JvmStatic
        public fun scalable(width: Int, height: Int): EntityDimensions = scalable(width.toFloat(), height.toFloat())

        /**
         * Creates new entity dimensions that can be scaled with [scale], with
         * the given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new scalable entity dimensions
         */
        @JvmStatic
        public fun scalable(width: Long, height: Long): EntityDimensions = scalable(width.toFloat(), height.toFloat())

        /**
         * Creates new entity dimensions that cannot scaled with [scale], with
         * the given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new fixed entity dimensions
         */
        @JvmStatic
        public fun fixed(width: Float, height: Float): EntityDimensions = EntityDimensions(width, height, true)

        /**
         * Creates new entity dimensions that cannot scaled with [scale], with
         * the given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new fixed entity dimensions
         */
        @JvmStatic
        public fun fixed(width: Double, height: Double): EntityDimensions = fixed(width.toFloat(), height.toFloat())

        /**
         * Creates new entity dimensions that cannot scaled with [scale], with
         * the given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new fixed entity dimensions
         */
        @JvmStatic
        public fun fixed(width: Int, height: Int): EntityDimensions = fixed(width.toFloat(), height.toFloat())

        /**
         * Creates new entity dimensions that cannot scaled with [scale], with
         * the given [width] and [height].
         *
         * @param width the width
         * @param height the height
         * @return new fixed entity dimensions
         */
        @JvmStatic
        public fun fixed(width: Long, height: Long): EntityDimensions = fixed(width.toFloat(), height.toFloat())
    }
}
