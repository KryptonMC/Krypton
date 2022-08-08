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
package org.kryptonmc.krypton.util.math

import org.kryptonmc.api.util.Direction

enum class AxisCycle {

    NONE {

        override fun cycle(axis: Direction.Axis): Direction.Axis = axis

        override fun inverse(): AxisCycle = this
    },
    FORWARD {

        override fun cycle(axis: Direction.Axis): Direction.Axis = cycleDefault(axis)

        override fun inverse(): AxisCycle = BACKWARD
    },
    BACKWARD {

        override fun cycle(axis: Direction.Axis): Direction.Axis = cycleDefault(axis)

        override fun inverse(): AxisCycle = FORWARD
    };

    fun cycle(x: Int, y: Int, z: Int, axis: Direction.Axis): Int = axis.select(x, y, z)

    fun cycle(x: Double, y: Double, z: Double, axis: Direction.Axis): Double = axis.select(x, y, z)

    abstract fun cycle(axis: Direction.Axis): Direction.Axis

    abstract fun inverse(): AxisCycle

    companion object {

        @JvmField
        val AXIS_VALUES: Array<Direction.Axis> = Direction.Axis.values()
        private val VALUES = values()

        @JvmStatic
        fun between(from: Direction.Axis, to: Direction.Axis): AxisCycle = VALUES[Math.floorMod(to.ordinal - from.ordinal, 3)]

        @JvmStatic
        private fun cycleDefault(axis: Direction.Axis): Direction.Axis = AXIS_VALUES[Math.floorMod(axis.ordinal - 1, 3)]
    }
}
