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
