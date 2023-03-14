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
