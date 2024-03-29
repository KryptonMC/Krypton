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

import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3i
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

    override fun shapeUpdate(direction: Direction, state: KryptonBlockState, pos: Vec3i, neighbourPos: Vec3i, flags: Int,
                             recursionLeft: Int) {
        addAndRun(pos, ShapeUpdate(direction, state, pos, neighbourPos, flags))
    }

    override fun neighbourChanged(pos: Vec3i, block: KryptonBlock, neighbourPos: Vec3i) {
        addAndRun(pos, SimpleNeighbourUpdate(pos, block, neighbourPos))
    }

    override fun neighbourChanged(state: KryptonBlockState, pos: Vec3i, block: KryptonBlock, neighbourPos: Vec3i, moving: Boolean) {
        addAndRun(pos, FullNeighbourUpdate(state, pos, block, neighbourPos, moving))
    }

    override fun updateNeighboursAtExceptFromFacing(pos: Vec3i, block: KryptonBlock, except: Direction?) {
        addAndRun(pos, MultiNeighbourUpdate(pos, block, except))
    }

    private fun addAndRun(pos: Vec3i, updates: NeighbourUpdates) {
        val haveUpdates = count > 0
        val tooManyUpdates = maxChainedUpdates >= 0 && count >= maxChainedUpdates
        ++count
        if (!tooManyUpdates) {
            if (haveUpdates) addedThisLayer.add(updates) else stack.push(updates)
        } else if (count - 1 == maxChainedUpdates) {
            LOGGER.error("Too many chained neighbour updates! Skipping the rest. First skipped position: $pos")
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

    private class FullNeighbourUpdate(private val state: KryptonBlockState, private val pos: Vec3i, private val block: KryptonBlock,
                                      private val neighbourPos: Vec3i, private val movedByPiston: Boolean) : NeighbourUpdates {

        override fun runNext(world: KryptonWorld): Boolean {
            NeighbourUpdater.executeUpdate(world, state, pos, block, neighbourPos, movedByPiston)
            return false
        }
    }

    private class MultiNeighbourUpdate(private val sourcePos: Vec3i, private val sourceBlock: KryptonBlock,
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

    private class ShapeUpdate(private val direction: Direction, private val state: KryptonBlockState, private val pos: Vec3i,
                              private val neighbourPos: Vec3i, private val updateFlags: Int) : NeighbourUpdates {

        override fun runNext(world: KryptonWorld): Boolean {
            NeighbourUpdater.executeShapeUpdate(world, direction, state, pos, neighbourPos, updateFlags, 512)
            return false
        }
    }

    private class SimpleNeighbourUpdate(private val pos: Vec3i, private val block: KryptonBlock,
                                        private val neighbourPos: Vec3i) : NeighbourUpdates {

        override fun runNext(world: KryptonWorld): Boolean {
            NeighbourUpdater.executeUpdate(world, world.getBlock(pos), pos, block, neighbourPos, false)
            return false
        }
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
    }
}
