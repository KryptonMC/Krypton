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
import kotlin.math.abs

object Directions {

    private val BY_3D_DATA = arrayOf(
        Direction.DOWN,
        Direction.UP,
        Direction.NORTH,
        Direction.SOUTH,
        Direction.WEST,
        Direction.EAST
    )
    private val BY_2D_DATA = arrayOf(
        Direction.SOUTH,
        Direction.WEST,
        Direction.NORTH,
        Direction.EAST
    )
    private val TO_2D_DATA = Object2IntArrayMap<Direction>().apply {
        BY_2D_DATA.forEachIndexed { index, direction -> put(direction, index) }
    }

    @JvmStatic
    fun ofPitch(pitch: Double): Direction = of2D((pitch / 90.0 + 0.5).floor() and 3)

    @JvmStatic
    fun of2D(data: Int): Direction = BY_2D_DATA[abs(data % BY_2D_DATA.size)]

    @JvmStatic
    fun of3D(data: Int): Direction = BY_3D_DATA[abs(data % BY_3D_DATA.size)]

    @JvmStatic
    fun data2D(direction: Direction): Int = TO_2D_DATA.getInt(direction)
}
