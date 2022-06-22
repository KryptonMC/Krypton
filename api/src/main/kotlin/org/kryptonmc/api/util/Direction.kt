/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import org.spongepowered.math.vector.Vector3i

/**
 * Represents a three-dimensional
 * [Cardinal direction](https://en.wikipedia.org/wiki/Cardinal_direction).
 *
 * @param axis the axis of this direction
 * @param axisDirection the direction of the axis of this direction
 * @param normal the normal of this direction
 */
public enum class Direction(
    @get:JvmName("axis") public val axis: Axis,
    @get:JvmName("axisDirection") public val axisDirection: AxisDirection,
    @get:JvmName("normal") public val normal: Vector3i
) {

    DOWN(Axis.Y, AxisDirection.NEGATIVE, Vector3i(0, -1, 0)),
    UP(Axis.Y, AxisDirection.POSITIVE, Vector3i(0, 1, 0)),
    NORTH(Axis.Z, AxisDirection.NEGATIVE, Vector3i(0, 0, -1)),
    SOUTH(Axis.Z, AxisDirection.POSITIVE, Vector3i(0, 0, 1)),
    WEST(Axis.X, AxisDirection.NEGATIVE, Vector3i(-1, 0, 0)),
    EAST(Axis.X, AxisDirection.POSITIVE, Vector3i(1, 0, 0));

    /**
     * The normal on the X axis.
     */
    public val normalX: Int
        @JvmName("normalX") get() = normal.x()

    /**
     * The normal on the Y axis.
     */
    public val normalY: Int
        @JvmName("normalY") get() = normal.y()

    /**
     * The normal on the Z axis.
     */
    public val normalZ: Int
        @JvmName("normalZ") get() = normal.z()

    /**
     * The opposite of this direction.
     *
     * Initialized lazily to avoid a circular dependency.
     */
    @get:JvmName("opposite")
    public val opposite: Direction by lazy {
        when (this) {
            DOWN -> UP
            UP -> DOWN
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }
    }

    /**
     * Axes that a direction may be on.
     */
    public enum class Axis {

        X {

            override fun select(x: Int, y: Int, z: Int): Int = x

            override fun select(x: Double, y: Double, z: Double): Double = x
        },
        Y {

            override fun select(x: Int, y: Int, z: Int): Int = y

            override fun select(x: Double, y: Double, z: Double): Double = y
        },
        Z {

            override fun select(x: Int, y: Int, z: Int): Int = z

            override fun select(x: Double, y: Double, z: Double): Double = z
        };

        /**
         * If this axis tiles vertically.
         */
        public val isVertical: Boolean
            get() = this == Y

        /**
         * If this axis tiles horizontally.
         */
        public val isHorizontal: Boolean
            get() = this == X || this == Z

        /**
         * Selects the appropriate [x], [y], or [z] coordinate, depending on
         * what the axis is, and returns it.
         *
         * @param x the X coordinate
         * @param y the Y coordinate
         * @param z the Z coordinate
         * @return the chosen coordinate
         */
        public abstract fun select(x: Int, y: Int, z: Int): Int

        /**
         * Selects the appropriate [x], [y], or [z] coordinate, depending on
         * what the axis is, and returns it.
         *
         * @param x the X coordinate
         * @param y the Y coordinate
         * @param z the Z coordinate
         * @return the chosen coordinate
         */
        public abstract fun select(x: Double, y: Double, z: Double): Double
    }

    /**
     * The direction of an [Axis] on a plane.
     *
     * @param step the step
     */
    public enum class AxisDirection(@get:JvmName("step") public val step: Int) {

        POSITIVE(1),
        NEGATIVE(-1);

        /**
         * The opposite direction.
         *
         * Initialized lazily to avoid circular dependencies.
         */
        @get:JvmName("opposite")
        public val opposite: AxisDirection by lazy { if (this == POSITIVE) NEGATIVE else POSITIVE }
    }
}
