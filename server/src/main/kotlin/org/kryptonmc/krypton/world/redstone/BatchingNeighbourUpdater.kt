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
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import java.util.ArrayDeque

/**
 * A neighbour updater that batches update requests and executes multiple updates at once.
 */
class BatchingNeighbourUpdater(private val world: KryptonWorld, private val maxChainedUpdates: Int) : NeighbourUpdater {

    private val stack = ArrayDeque<NeighbourUpdates>()
    private val addedThisLayer = ArrayList<NeighbourUpdates>()
    private var count = 0

    override fun shapeUpdate(direction: Direction, state: KryptonBlockState, pos: BlockPos, neighbourPos: BlockPos, flags: Int, recursionLeft: Int) {
        addAndRun(pos, ShapeUpdate(direction, state, pos.immutable(), neighbourPos.immutable(), flags))
    }

    override fun neighbourChanged(pos: BlockPos, block: KryptonBlock, neighbourPos: BlockPos) {
        addAndRun(pos, SimpleNeighbourUpdate(pos, block, neighbourPos))
    }

    override fun neighbourChanged(state: KryptonBlockState, pos: BlockPos, block: KryptonBlock, neighbourPos: BlockPos, moving: Boolean) {
        addAndRun(pos, FullNeighbourUpdate(state, pos.immutable(), block, neighbourPos.immutable(), moving))
    }

    override fun updateNeighboursAtExceptFromFacing(pos: BlockPos, block: KryptonBlock, except: Direction?) {
        addAndRun(pos, MultiNeighbourUpdate(pos.immutable(), block, except))
    }

    private fun addAndRun(pos: BlockPos, updates: NeighbourUpdates) {
        val haveUpdates = count > 0
        val tooManyUpdates = maxChainedUpdates >= 0 && count >= maxChainedUpdates
        ++count
        if (!tooManyUpdates) {
            if (haveUpdates) addedThisLayer.add(updates) else stack.push(updates)
        } else if (count - 1 == maxChainedUpdates) {
            LOGGER.error("Too many chained neighbour updates! Skipping the rest. First skipped position: ${pos.toShortString()}")
        }
        if (!haveUpdates) runUpdates()
    }

    private fun runUpdates() {
        try {
            while (!stack.isEmpty() || addedThisLayer.isNotEmpty()) {
                for (i in addedThisLayer.size - 1 downTo 0) {
                    stack.push(addedThisLayer.get(i))
                }
                addedThisLayer.clear()
                val topUpdate = stack.peek()
                while (addedThisLayer.isEmpty()) {
                    if (!topUpdate.runNext(world)) {
                        stack.pop()
                        break
                    }
                }
            }
        } finally {
            stack.clear()
            addedThisLayer.clear()
            count = 0
        }
    }

    private sealed interface NeighbourUpdates {

        fun runNext(world: KryptonWorld): Boolean
    }

    private class FullNeighbourUpdate(private val state: KryptonBlockState, private val pos: BlockPos, private val block: KryptonBlock,
                                      private val neighbourPos: BlockPos, private val movedByPiston: Boolean) : NeighbourUpdates {

        override fun runNext(world: KryptonWorld): Boolean {
            NeighbourUpdater.executeUpdate(world, state, pos, block, neighbourPos, movedByPiston)
            return false
        }
    }

    private class MultiNeighbourUpdate(private val sourcePos: BlockPos, private val sourceBlock: KryptonBlock,
                                       private val skipDirection: Direction?) : NeighbourUpdates {

        private var index = 0

        init {
            if (NeighbourUpdater.UPDATE_ORDER[index] == skipDirection) ++index
        }

        override fun runNext(world: KryptonWorld): Boolean {
            val relative = sourcePos.relative(NeighbourUpdater.UPDATE_ORDER[index++])
            val state = world.getBlock(relative)
            state.neighbourChanged(world, relative, sourceBlock, sourcePos, false)
            if (index < NeighbourUpdater.UPDATE_ORDER.size && NeighbourUpdater.UPDATE_ORDER[index] == skipDirection) ++index
            return index < NeighbourUpdater.UPDATE_ORDER.size
        }
    }

    private class ShapeUpdate(private val direction: Direction, private val state: KryptonBlockState, private val pos: BlockPos,
                              private val neighbourPos: BlockPos, private val updateFlags: Int) : NeighbourUpdates {

        override fun runNext(world: KryptonWorld): Boolean {
            NeighbourUpdater.executeShapeUpdate(world, direction, state, pos, neighbourPos, updateFlags, 512)
            return false
        }
    }

    private class SimpleNeighbourUpdate(private val pos: BlockPos, private val block: KryptonBlock,
                                        private val neighbourPos: BlockPos) : NeighbourUpdates {

        override fun runNext(world: KryptonWorld): Boolean {
            NeighbourUpdater.executeUpdate(world, world.getBlock(pos), pos, block, neighbourPos, false)
            return false
        }
    }

    companion object {

        private val LOGGER = logger<BatchingNeighbourUpdater>()
    }
}
