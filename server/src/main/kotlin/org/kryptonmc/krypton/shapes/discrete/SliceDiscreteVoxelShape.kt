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
package org.kryptonmc.krypton.shapes.discrete

import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.util.math.Maths

class SliceDiscreteVoxelShape(
    private val parent: DiscreteVoxelShape,
    private val startX: Int,
    private val startY: Int,
    private val startZ: Int,
    private val endX: Int,
    private val endY: Int,
    private val endZ: Int
) : DiscreteVoxelShape(endX - startX, endY - startY, endZ - startZ) {

    override fun isFull(x: Int, y: Int, z: Int): Boolean = parent.isFull(startX + x, startY + y, startZ + z)

    override fun fill(x: Int, y: Int, z: Int) {
        parent.fill(startX + x, startY + y, startZ + z)
    }

    override fun firstFull(axis: Direction.Axis): Int = clampToShape(axis, parent.firstFull(axis))

    override fun lastFull(axis: Direction.Axis): Int = clampToShape(axis, parent.lastFull(axis))

    private fun clampToShape(axis: Direction.Axis, value: Int): Int {
        val low = axis.select(startX, startY, startZ)
        val high = axis.select(endX, endY, endZ)
        return Maths.clamp(value, low, high) - low
    }

    override fun toString(): String = "SubShape(parent=$parent, sizeX=$sizeX, sizeY=$sizeY, sizeZ=$sizeZ, startX=$startX, startY=$startY," +
            "startZ=$startZ, endX=$endX, endY=$endY, endZ=$endZ)"
}
