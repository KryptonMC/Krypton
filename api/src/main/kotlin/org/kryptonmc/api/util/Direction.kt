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

/**
 * Represents a three-dimensional
 * [Cardinal direction](https://en.wikipedia.org/wiki/Cardinal_direction).
 */
public enum class Direction(
    private val oppositeIndex: Int,
    /**
     * The axis of this direction.
     */
    public val axis: Axis,
    /**
     * The direction of the axis of this direction.
     */
    public val axisDirection: AxisDirection,
    /**
     * The normal of this direction.
     */
    public val normal: Vec3i
) {

    DOWN(1, Axis.Y, AxisDirection.NEGATIVE, Vec3i(0, -1, 0)),
    UP(0, Axis.Y, AxisDirection.POSITIVE, Vec3i(0, 1, 0)),
    NORTH(3, Axis.Z, AxisDirection.NEGATIVE, Vec3i(0, 0, -1)),
    SOUTH(2, Axis.Z, AxisDirection.POSITIVE, Vec3i(0, 0, 1)),
    WEST(5, Axis.X, AxisDirection.NEGATIVE, Vec3i(-1, 0, 0)),
    EAST(4, Axis.X, AxisDirection.POSITIVE, Vec3i(1, 0, 0));

    /**
     * The normal on the X axis.
     */
    public val normalX: Int
        get() = normal.x

    /**
     * The normal on the Y axis.
     */
    public val normalY: Int
        get() = normal.y

    /**
     * The normal on the Z axis.
     */
    public val normalZ: Int
        get() = normal.z

    /**
     * The opposite of this direction.
     *
     * Initialized lazily to avoid a circular dependency.
     */
    public val opposite: Direction
        get() = VALUES[oppositeIndex]

    /**
     * Axes that a direction may be on.
     */
    public enum class Axis(private val intSelector: IntSelector, private val doubleSelector: DoubleSelector) {

        X({ x, _, _ -> x }, { x, _, _ -> x }),
        Y({ _, y, _ -> y }, { _, y, _ -> y }),
        Z({ _, _, z -> z }, { _, _, z -> z });

        /**
         * Checks if this axis tiles vertically.
         *
         * @return true if this axis is vertical, false otherwise
         */
        public fun isVertical(): Boolean = this == Y

        /**
         * Checks if this axis tiles horizontally.
         *
         * @return true if this axis is horizontal, false otherwise
         */
        public fun isHorizontal(): Boolean = this == X || this == Z

        /**
         * Selects the appropriate [x], [y], or [z] coordinate, depending on
         * what the axis is, and returns it.
         *
         * @param x the X coordinate
         * @param y the Y coordinate
         * @param z the Z coordinate
         * @return the chosen coordinate
         */
        public fun select(x: Int, y: Int, z: Int): Int = intSelector.select(x, y, z)

        /**
         * Selects the appropriate [x], [y], or [z] coordinate, depending on
         * what the axis is, and returns it.
         *
         * @param x the X coordinate
         * @param y the Y coordinate
         * @param z the Z coordinate
         * @return the chosen coordinate
         */
        public fun select(x: Double, y: Double, z: Double): Double = doubleSelector.select(x, y, z)
    }

    /**
     * The direction of an [Axis] on a plane.
     *
     * @param step the step
     */
    public enum class AxisDirection(public val step: Int) {

        POSITIVE(1),
        NEGATIVE(-1);

        /**
         * The opposite direction.
         *
         * Initialized lazily to avoid circular dependencies.
         */
        public val opposite: AxisDirection
            get() = if (this === POSITIVE) NEGATIVE else POSITIVE
    }

    private fun interface IntSelector {

        fun select(x: Int, y: Int, z: Int): Int
    }

    private fun interface DoubleSelector {

        fun select(x: Double, y: Double, z: Double): Double
    }

    public companion object {

        private val VALUES = values()
    }
}
