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
