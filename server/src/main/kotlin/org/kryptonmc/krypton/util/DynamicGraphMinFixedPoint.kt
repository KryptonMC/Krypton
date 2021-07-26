/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.util

import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet
import kotlin.math.min

abstract class DynamicGraphMinFixedPoint(
    private val levelCount: Int,
    private val expectedLevelSize: Int,
    private val expectedTotalSize: Int
) {

    private val queues = Array<LongLinkedOpenHashSet>(levelCount) {
        object : LongLinkedOpenHashSet(expectedLevelSize, 0.5F) {
            override fun rehash(newN: Int) {
                if (newN > expectedLevelSize) super.rehash(newN)
            }
        }
    }
    private val computedLevels = object : Long2ByteOpenHashMap(expectedTotalSize, 0.5F) {
        override fun getOrDefault(key: Long, defaultValue: Byte): Byte = super.getOrDefault(key, defaultValue)
        override fun rehash(newN: Int) {
            if (newN > expectedTotalSize) super.rehash(newN)
        }
    }.apply { defaultReturnValue(-1) }
    private var firstQueuedLevel = levelCount
    @Volatile protected var hasWork = false
        private set

    val queueSize: Int
        get() = computedLevels.size

    init {
        require(levelCount < NO_COMPUTED_LEVEL - 1) { "Level count must be less than the maximum computed level (255) - 1!" }
    }

    protected abstract fun isSource(id: Long): Boolean

    protected abstract fun getComputedLevel(id: Long, excludedId: Long, maxLevel: Int): Int

    protected abstract fun checkNeighbours(id: Long, level: Int, decrease: Boolean)

    protected abstract fun getLevel(id: Long): Int

    protected abstract fun setLevel(id: Long, level: Int)

    protected abstract fun computeLevelFromNeighbour(sourceId: Long, targetId: Long, level: Int): Int

    protected fun checkEdge(sourceId: Long, id: Long, level: Int, decrease: Boolean) {
        checkEdge(sourceId, id, level, getLevel(id), computedLevels[id].toInt() and NO_COMPUTED_LEVEL, decrease)
        hasWork = firstQueuedLevel < levelCount
    }

    protected fun checkNeighbour(sourceId: Long, targetId: Long, level: Int, decrease: Boolean) {
        val cached = computedLevels[targetId].toInt() and NO_COMPUTED_LEVEL
        val neighbour = computeLevelFromNeighbour(sourceId, targetId, level).clamp(0, levelCount - 1)
        if (decrease) {
            checkEdge(sourceId, targetId, neighbour, getLevel(targetId), cached, true)
            return
        }
        val computed = if (cached == NO_COMPUTED_LEVEL) getLevel(targetId).clamp(0, levelCount - 1) else cached
        val notComputed = cached == NO_COMPUTED_LEVEL
        if (neighbour == computed) checkEdge(sourceId, targetId, levelCount - 1, if (notComputed) computed else getLevel(targetId), cached, false)
    }

    protected fun runUpdates(maxSteps: Int): Int {
        if (firstQueuedLevel >= levelCount) return maxSteps
        var temp = maxSteps
        while (firstQueuedLevel < levelCount && temp > 0) {
            --temp
            val queue = queues[firstQueuedLevel]
            val firstId = queue.removeFirstLong()
            val level = getLevel(firstId).clamp(0, levelCount - 1)
            if (queue.isEmpty()) checkFirstQueuedLevel(levelCount)
            val cached = computedLevels.remove(firstId).toInt() and 255
            if (cached < level) {
                setLevel(firstId, cached)
                checkNeighbours(firstId, cached, true)
            } else if (cached > level) {
                queue(firstId, cached, getKey(levelCount - 1, cached))
                setLevel(firstId, levelCount - 1)
                checkNeighbours(firstId, level, false)
            }
        }
        hasWork = firstQueuedLevel < levelCount
        return temp
    }

    private fun checkEdge(sourceId: Long, id: Long, level: Int, current: Int, pending: Int, decrease: Boolean) {
        if (isSource(id)) return
        val lvl = level.clamp(0, levelCount - 1)
        val currentLevel = current.clamp(0, levelCount - 1)
        val notComputed = pending == NO_COMPUTED_LEVEL
        val pendingLevel = if (notComputed) currentLevel else pending
        val newLevel = if (decrease) min(pendingLevel, lvl) else getComputedLevel(id, sourceId, level).clamp(0, levelCount - 1)
        val key = getKey(currentLevel, pendingLevel)
        if (currentLevel != newLevel) {
            val l = getKey(currentLevel, newLevel)
            if (key != l && notComputed) dequeue(id, key, l, false)
            queue(id, newLevel, l)
        } else if (!notComputed) {
            dequeue(id, key, levelCount, true)
        }
    }

    private fun queue(id: Long, level: Int, target: Int) {
        computedLevels[id] = level.toByte()
        queues[target].add(id)
        if (firstQueuedLevel > target) firstQueuedLevel = target
    }

    private fun dequeue(id: Long, level: Int, maxLevel: Int, removeFully: Boolean) {
        if (removeFully) computedLevels.remove(id)
        queues[level].remove(id)
        if (queues[level].isEmpty() && firstQueuedLevel == level) checkFirstQueuedLevel(maxLevel)
    }

    private fun getKey(a: Int, b: Int): Int {
        var i = a
        if (a > b) i = b
        if (i > levelCount - 1) i = levelCount - 1
        return i
    }

    private fun checkFirstQueuedLevel(max: Int) {
        val oldFirstQueuedLevel = firstQueuedLevel
        firstQueuedLevel = max
        for (i in oldFirstQueuedLevel + 1 until max) {
            if (queues[i].isNotEmpty()) {
                firstQueuedLevel = i
                break
            }
        }
    }

    companion object {

        private const val NO_COMPUTED_LEVEL = 255
    }
}
