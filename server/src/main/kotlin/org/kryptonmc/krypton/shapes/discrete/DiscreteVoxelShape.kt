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
package org.kryptonmc.krypton.shapes.discrete

import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.util.math.AxisCycle

abstract class DiscreteVoxelShape protected constructor(val sizeX: Int, val sizeY: Int, val sizeZ: Int) {

    init {
        require(sizeX >= 0 && sizeY >= 0 && sizeZ >= 0) { "All sizes must be positive! X: $sizeX, Y: $sizeY, Z: $sizeZ" }
    }

    abstract fun isFull(x: Int, y: Int, z: Int): Boolean

    private fun isFull(cycle: AxisCycle, x: Int, y: Int, z: Int): Boolean =
        isFull(cycle.cycle(x, y, z, Direction.Axis.X), cycle.cycle(x, y, z, Direction.Axis.Y), cycle.cycle(x, y, z, Direction.Axis.Z))

    fun isFullWide(x: Int, y: Int, z: Int): Boolean {
        if (x >= 0 && y >= 0 && z >= 0) {
            if (x < sizeX && y < sizeY && z < sizeZ) return isFull(x, y, z)
            return false
        }
        return false
    }

    fun isFullWide(cycle: AxisCycle, x: Int, y: Int, z: Int): Boolean =
        isFullWide(cycle.cycle(x, y, x, Direction.Axis.X), cycle.cycle(x, y, z, Direction.Axis.Y), cycle.cycle(x, y, z, Direction.Axis.Z))

    abstract fun fill(x: Int, y: Int, z: Int)

    open fun isEmpty(): Boolean = AXIS_VALUES.any { firstFull(it) >= lastFull(it) }

    abstract fun firstFull(axis: Direction.Axis): Int

    abstract fun lastFull(axis: Direction.Axis): Int

    fun firstFull(axis: Direction.Axis, y: Int, z: Int): Int {
        val size = size(axis)
        if (y < 0 || z < 0) return size
        val forward = AxisCycle.FORWARD.cycle(axis)
        val backward = AxisCycle.BACKWARD.cycle(axis)
        if (y >= size(forward) || z >= size(backward)) return size
        val cycle = AxisCycle.between(Direction.Axis.X, axis)
        for (x in 0 until size) {
            if (isFull(cycle, x, y, z)) return x
        }
        return size
    }

    fun lastFull(axis: Direction.Axis, y: Int, z: Int): Int {
        if (y < 0 || z < 0) return 0
        val forward = AxisCycle.FORWARD.cycle(axis)
        val backward = AxisCycle.BACKWARD.cycle(axis)
        if (y >= size(forward) || z >= size(backward)) return 0
        val size = size(axis)
        val cycle = AxisCycle.between(Direction.Axis.X, axis)
        for (x in size - 1 downTo 0) {
            if (isFull(cycle, x, y, z)) return x + 1
        }
        return 0
    }

    fun size(axis: Direction.Axis): Int = axis.select(sizeX, sizeY, sizeZ)

    fun sizeX(): Int = size(Direction.Axis.X)

    fun sizeY(): Int = size(Direction.Axis.Y)

    fun sizeZ(): Int = size(Direction.Axis.Z)

    fun forAllBoxes(combine: Boolean, consumer: IntLineConsumer) {
        BitSetDiscreteVoxelShape.forAllBoxes(this, consumer, combine)
    }

    fun interface IntLineConsumer {

        fun consume(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int)
    }

    companion object {

        private val AXIS_VALUES = Direction.Axis.values()
    }
}
