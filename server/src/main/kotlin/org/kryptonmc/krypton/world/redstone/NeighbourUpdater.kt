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
package org.kryptonmc.krypton.world.redstone

import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.components.WorldAccessor

/**
 * An abstraction over updating neighbours of a block.
 */
interface NeighbourUpdater {

    fun shapeUpdate(direction: Direction, state: KryptonBlockState, pos: Vec3i, neighbourPos: Vec3i, flags: Int, recursionLeft: Int)

    fun neighbourChanged(pos: Vec3i, block: KryptonBlock, neighbourPos: Vec3i)

    fun neighbourChanged(state: KryptonBlockState, pos: Vec3i, block: KryptonBlock, neighbourPos: Vec3i, moving: Boolean)

    fun updateNeighboursAtExceptFromFacing(pos: Vec3i, block: KryptonBlock, except: Direction?) {
        UPDATE_ORDER.forEach { if (it != except) neighbourChanged(pos.relative(it), block, pos) }
    }

    companion object {

        @JvmField
        val UPDATE_ORDER: Array<Direction> = arrayOf(Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH)

        @JvmStatic
        fun executeShapeUpdate(world: WorldAccessor, direction: Direction, state: KryptonBlockState, pos: Vec3i, neighbourPos: Vec3i,
                               flags: Int, recursionLeft: Int) {
            val oldState = world.getBlock(pos)
            val newState = oldState.updateShape(direction, state, world, pos, neighbourPos)
            KryptonBlock.updateOrDestroy(oldState, newState, world, pos, flags, recursionLeft)
        }

        @JvmStatic
        fun executeUpdate(world: KryptonWorld, state: KryptonBlockState, pos: Vec3i, block: KryptonBlock, neighbourPos: Vec3i,
                          moving: Boolean) {
            state.neighbourChanged(world, pos, block, neighbourPos, moving)
        }
    }
}
