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
package org.kryptonmc.krypton.util

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Direction.Axis
import org.kryptonmc.api.util.Direction.AxisDirection
import java.util.function.Predicate
import kotlin.math.abs

object Directions {

    private val BY_3D_DATA = arrayOf(Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST)
    private val BY_2D_DATA = arrayOf(Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST)
    private val TO_2D_DATA = Object2IntArrayMap<Direction>().apply { BY_2D_DATA.forEachIndexed { index, direction -> put(direction, index) } }

    @JvmStatic
    fun ofPitch(pitch: Double): Direction = of2D(Maths.floor(pitch / 90.0 + 0.5) and 3)

    @JvmStatic
    fun of2D(data: Int): Direction = BY_2D_DATA[abs(data % BY_2D_DATA.size)]

    @JvmStatic
    fun of3D(data: Int): Direction = BY_3D_DATA[abs(data % BY_3D_DATA.size)]

    @JvmStatic
    fun data2D(direction: Direction): Int = TO_2D_DATA.getInt(direction)

    @JvmStatic
    fun clockwise(direction: Direction): Direction = when (direction) {
        Direction.NORTH -> Direction.EAST
        Direction.SOUTH -> Direction.WEST
        Direction.WEST -> Direction.NORTH
        Direction.EAST -> Direction.SOUTH
        else -> error("Unable to get clockwise direction for given direction $this!")
    }

    @JvmStatic
    fun antiClockwise(direction: Direction): Direction = when (direction) {
        Direction.NORTH -> Direction.WEST
        Direction.SOUTH -> Direction.EAST
        Direction.WEST -> Direction.SOUTH
        Direction.EAST -> Direction.NORTH
        else -> error("Unable to get anti clockwise direction for given direction $this!")
    }

    @JvmStatic
    fun fromAxisAndDirection(axis: Axis, direction: AxisDirection): Direction = when (axis) {
        Axis.X -> if (direction == AxisDirection.POSITIVE) Direction.EAST else Direction.WEST
        Axis.Y -> if (direction == AxisDirection.POSITIVE) Direction.SOUTH else Direction.NORTH
        Axis.Z -> if (direction == AxisDirection.POSITIVE) Direction.UP else Direction.DOWN
    }

    @JvmStatic
    private fun plane(axis: Axis): Plane = when (axis) {
        Axis.X, Axis.Z -> Plane.HORIZONTAL
        Axis.Y -> Plane.VERTICAL
    }

    enum class Plane(private val faces: Array<Direction>, private val axes: Array<Axis>) : Iterable<Direction>, Predicate<Direction> {

        HORIZONTAL(arrayOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST), arrayOf(Axis.X, Axis.Z)),
        VERTICAL(arrayOf(Direction.UP, Direction.DOWN), arrayOf(Axis.Y));

        override fun test(t: Direction): Boolean = plane(t.axis) == this

        override fun iterator(): Iterator<Direction> = NoSpread.iteratorsForArray(faces)
    }
}
