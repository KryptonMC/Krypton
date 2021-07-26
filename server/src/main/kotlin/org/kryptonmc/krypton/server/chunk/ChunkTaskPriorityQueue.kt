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
package org.kryptonmc.krypton.server.chunk

import com.mojang.datafixers.util.Either
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import org.kryptonmc.krypton.world.chunk.ChunkManager
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import kotlin.math.min

class ChunkTaskPriorityQueue<T>(private val name: String, private val maxTasks: Int) {

    private val taskQueue: List<Long2ObjectLinkedOpenHashMap<MutableList<T?>>> = (0..PRIORITY_LEVEL_COUNT).map { Long2ObjectLinkedOpenHashMap() }
    private var firstQueue = PRIORITY_LEVEL_COUNT
    val acquired = LongOpenHashSet()

    fun resort(newPriority: Int, oldPriority: Int, position: ChunkPosition) {
        if (newPriority >= PRIORITY_LEVEL_COUNT) return
        val queue = taskQueue[newPriority]
        val first = queue.remove(position.toLong())
        if (newPriority == firstQueue) while (firstQueue < PRIORITY_LEVEL_COUNT && taskQueue[firstQueue].isEmpty()) firstQueue++
        if (first != null && first.isNotEmpty()) {
            taskQueue[oldPriority].getOrPut(position.toLong()) { mutableListOf() }.addAll(first)
            firstQueue = min(firstQueue, oldPriority)
        }
    }

    fun submit(id: Long, priority: Int, task: T?) {
        taskQueue[priority].getOrPut(id) { mutableListOf() }.add(task)
        firstQueue = min(firstQueue, priority)
    }

    fun release(id: Long, clearQueue: Boolean) {
        taskQueue.forEach { queue ->
            val tasks = queue[id] ?: return@forEach
            if (clearQueue) tasks.clear() else tasks.removeIf { it == null }
            if (tasks.isEmpty()) queue.remove(id)
        }
        while (firstQueue < PRIORITY_LEVEL_COUNT && taskQueue[firstQueue].isEmpty()) firstQueue++
        acquired.remove(id)
    }

    fun pop(): Sequence<Either<T, Runnable>>? {
        if (acquired.size >= maxTasks) return null
        if (firstQueue >= PRIORITY_LEVEL_COUNT) return null
        val oldFirstQueue = firstQueue
        val queue = taskQueue[oldFirstQueue]
        val firstKey = queue.firstLongKey()
        val firstTaskList = queue.removeFirst()
        while (firstQueue < PRIORITY_LEVEL_COUNT && taskQueue[firstQueue].isEmpty()) firstQueue++
        return firstTaskList.asSequence().map { task -> task?.let { Either.left(it) } ?: Either.right(acquire(firstKey)) }
    }

    override fun toString() = "$name $firstQueue.."

    private fun acquire(id: Long) = Runnable { acquired.add(id) }

    companion object {

        val PRIORITY_LEVEL_COUNT = ChunkManager.MAX_DISTANCE + 2
    }
}
