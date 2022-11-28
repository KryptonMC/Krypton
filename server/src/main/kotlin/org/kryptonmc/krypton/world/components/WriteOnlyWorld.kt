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
package org.kryptonmc.krypton.world.components

import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.world.block.state.KryptonBlockState

interface WriteOnlyWorld {

    fun setBlock(pos: BlockPos, state: KryptonBlockState, flags: Int, recursionLeft: Int): Boolean

    fun setBlock(pos: BlockPos, state: KryptonBlockState, flags: Int): Boolean = setBlock(pos, state, flags, 512)

    fun removeBlock(pos: BlockPos, moving: Boolean): Boolean

    fun destroyBlock(pos: BlockPos, drop: Boolean, entity: KryptonEntity?, recursionLeft: Int): Boolean

    fun destroyBlock(pos: BlockPos, drop: Boolean, entity: KryptonEntity?): Boolean = destroyBlock(pos, drop, entity, 512)

    fun destroyBlock(pos: BlockPos, drop: Boolean): Boolean = destroyBlock(pos, drop, null)
}
