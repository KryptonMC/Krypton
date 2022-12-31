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
package org.kryptonmc.krypton.util.hit

import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.coordinate.BlockPos

class BlockHitResult private constructor(
    location: Vec3d,
    val direction: Direction,
    val position: BlockPos,
    private val miss: Boolean,
    val isInside: Boolean
) : HitResult(location) {

    override val type: Type
        get() = if (miss) Type.MISS else Type.BLOCK

    constructor(location: Vec3d, direction: Direction, position: BlockPos, inside: Boolean) : this(location, direction, position, false, inside)

    fun withDirection(direction: Direction): BlockHitResult = BlockHitResult(location, direction, position, miss, isInside)

    fun withPosition(position: BlockPos): BlockHitResult = BlockHitResult(location, direction, position, miss, isInside)

    companion object {

        @JvmStatic
        fun miss(location: Vec3d, direction: Direction, position: BlockPos): BlockHitResult =
            BlockHitResult(location, direction, position, true, false)
    }
}
