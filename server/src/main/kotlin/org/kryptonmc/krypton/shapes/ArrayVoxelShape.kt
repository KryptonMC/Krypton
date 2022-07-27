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
package org.kryptonmc.krypton.shapes

import it.unimi.dsi.fastutil.doubles.DoubleArrayList
import it.unimi.dsi.fastutil.doubles.DoubleList
import org.kryptonmc.api.util.Direction

class ArrayVoxelShape(
    shape: DiscreteVoxelShape,
    private val xs: DoubleList,
    private val ys: DoubleList,
    private val zs: DoubleList
) : VoxelShape(shape) {

    constructor(shape: DiscreteVoxelShape, xs: DoubleArray, ys: DoubleArray, zs: DoubleArray) : this(
        shape,
        DoubleArrayList.wrap(xs.copyOf(shape.sizeX() + 1)),
        DoubleArrayList.wrap(ys.copyOf(shape.sizeY() + 1)),
        DoubleArrayList.wrap(zs.copyOf(shape.sizeZ() + 1))
    )

    init {
        val sizeX = shape.sizeX() + 1
        val sizeY = shape.sizeY() + 1
        val sizeZ = shape.sizeZ() + 1
        require(sizeX == xs.size && sizeY == ys.size && sizeZ == zs.size) {
            "Lengths of point arrays must be consistent with the size of the VoxelShape!"
        }
    }

    override fun coordinates(axis: Direction.Axis): DoubleList = when (axis) {
        Direction.Axis.X -> xs
        Direction.Axis.Y -> ys
        Direction.Axis.Z -> zs
    }

    override fun toString(): String = "ArrayVoxelShape(shape=$shape, xs=$xs, ys=$ys, zs=$zs)"
}
