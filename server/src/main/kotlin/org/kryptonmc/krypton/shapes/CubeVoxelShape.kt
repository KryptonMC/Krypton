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
import org.kryptonmc.krypton.shapes.util.CubePointRange
import org.kryptonmc.krypton.util.math.Maths

class CubeVoxelShape(shape: DiscreteVoxelShape) : VoxelShape(shape) {

    override fun getCoordinates(axis: Direction.Axis): DoubleList = CubePointRange(shape.size(axis))

    override fun findIndex(axis: Direction.Axis, position: Double): Int {
        val size = shape.size(axis).toDouble()
        return Maths.floor(Maths.clamp(position * size, -1.0, size))
    }

    override fun toString(): String = "CubeVoxelShape(shape=$shape)"
}
