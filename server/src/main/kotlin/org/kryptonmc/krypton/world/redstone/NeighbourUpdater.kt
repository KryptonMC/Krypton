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
package org.kryptonmc.krypton.world.redstone

import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.coordinate.BlockPos
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.components.WorldAccessor

/**
 * An abstraction over updating neighbours of a block.
 */
interface NeighbourUpdater {

    fun shapeUpdate(direction: Direction, state: KryptonBlockState, pos: BlockPos, neighbourPos: BlockPos, flags: Int, recursionLeft: Int)

    fun neighbourChanged(pos: BlockPos, block: KryptonBlock, neighbourPos: BlockPos)

    fun neighbourChanged(state: KryptonBlockState, pos: BlockPos, block: KryptonBlock, neighbourPos: BlockPos, moving: Boolean)

    fun updateNeighboursAtExceptFromFacing(pos: BlockPos, block: KryptonBlock, except: Direction?) {
        UPDATE_ORDER.forEach { if (it != except) neighbourChanged(pos.relative(it), block, pos) }
    }

    companion object {

        @JvmField
        val UPDATE_ORDER: Array<Direction> = arrayOf(Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH)

        @JvmStatic
        fun executeShapeUpdate(world: WorldAccessor, direction: Direction, state: KryptonBlockState, pos: BlockPos, neighbourPos: BlockPos,
                               flags: Int, recursionLeft: Int) {
            val oldState = world.getBlock(pos)
            val newState = oldState.updateShape(direction, state, world, pos, neighbourPos)
            KryptonBlock.updateOrDestroy(oldState, newState, world, pos, flags, recursionLeft)
        }

        @JvmStatic
        fun executeUpdate(world: KryptonWorld, state: KryptonBlockState, pos: BlockPos, block: KryptonBlock, neighbourPos: BlockPos,
                          moving: Boolean) {
            state.neighbourChanged(world, pos, block, neighbourPos, moving)
        }
    }
}
