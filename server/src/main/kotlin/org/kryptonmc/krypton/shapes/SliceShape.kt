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

import it.unimi.dsi.fastutil.doubles.DoubleList
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.shapes.discrete.DiscreteVoxelShape
import org.kryptonmc.krypton.shapes.discrete.SliceDiscreteVoxelShape
import org.kryptonmc.krypton.shapes.util.CubePointRange

class SliceShape(
    private val delegate: VoxelShape,
    private val axis: Direction.Axis,
    index: Int
) : VoxelShape(makeSlice(delegate.shape, axis, index)) {

    override fun getCoordinates(axis: Direction.Axis): DoubleList = if (axis == this.axis) SLICE_COORDINATES else delegate.getCoordinates(axis)

    companion object {

        private val SLICE_COORDINATES = CubePointRange(1)

        @JvmStatic
        private fun makeSlice(parent: DiscreteVoxelShape, axis: Direction.Axis, index: Int): DiscreteVoxelShape {
            return SliceDiscreteVoxelShape(parent, axis.select(index, 0, 0), axis.select(0, index, 0), axis.select(0, 0, index),
                axis.select(index + 1, parent.sizeX, parent.sizeX), axis.select(parent.sizeY, index + 1, parent.sizeY),
                axis.select(parent.sizeZ, parent.sizeZ, index + 1))
        }
    }
}
