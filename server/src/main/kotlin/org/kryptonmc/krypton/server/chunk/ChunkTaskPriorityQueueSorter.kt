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

import org.kryptonmc.krypton.util.sequence
import org.kryptonmc.krypton.util.thread.FixedPriorityQueue
import org.kryptonmc.krypton.util.thread.PriorityTask
import org.kryptonmc.krypton.util.thread.Scheduler
import org.kryptonmc.krypton.util.thread.StandardScheduler
import org.kryptonmc.krypton.world.chunk.ChunkHolder
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.Function

class ChunkTaskPriorityQueueSorter(
    sleeping: List<Scheduler<*>>,
    executor: Executor,
    maxTasks: Int
) : (ChunkPosition, () -> Int, Int, (Int) -> Unit) -> Unit, AutoCloseable {

    private val queues: MutableMap<Scheduler<*>, ChunkTaskPriorityQueue<out Function<Scheduler<Unit>, *>>> = sleeping.associateWithTo(mutableMapOf()) { ChunkTaskPriorityQueue("${it.name}_queue", maxTasks) }
    private val sleeping = sleeping.toMutableSet()
    private val scheduler = StandardScheduler("sorter", executor, FixedPriorityQueue(4))

    fun <T> getScheduler(executor: Scheduler<T>, addBlocker: Boolean): Scheduler<Message<T>> = scheduler.submitFuture<Scheduler<Message<T>>> {
        PriorityTask(0) {
            getQueue(executor)
            it.submit(Scheduler.create("Chunk priority sorter for ${executor.name}") { submit(executor, it.task, it.position, it.level, addBlocker) })
        }
    }.join()

    fun getReleaseScheduler(executor: Scheduler<Runnable>): Scheduler<Release> = scheduler.submitFuture<Scheduler<Release>> {
        PriorityTask(0) { it.submit(Scheduler.create("Chunk priority sorter for ${executor.name}") { release(executor, it.position, it.task, it.clearQueue) }) }
    }.join()

    override fun invoke(position: ChunkPosition, levelSupplier: () -> Int, newLevel: Int, levelSetter: (Int) -> Unit) = scheduler.submit(PriorityTask(1) {
        val level = levelSupplier()
        queues.values.forEach { it.resort(level, newLevel, position) }
        levelSetter(newLevel)
    })

    override fun close() = queues.keys.forEach(Scheduler<*>::close)

    private fun <T> submit(executor: Scheduler<T>, task: Function<Scheduler<Unit>, T>, position: Long, levelSupplier: () -> Int, addBlocker: Boolean) = scheduler.submit(PriorityTask(2) {
        val queue = getQueue(executor)
        val level = levelSupplier()
        queue.submit(position, level, task)
        if (addBlocker) queue.submit(position, level, null)
        if (sleeping.remove(executor)) poll(queue, executor)
    })

    private fun <T> release(executor: Scheduler<T>, position: Long, task: Runnable, clearQueue: Boolean) = scheduler.submit(PriorityTask(1) {
        val queue = getQueue(executor)
        queue.release(position, clearQueue)
        if (sleeping.remove(executor)) poll(queue, executor)
        task.run()
    })

    private fun <T> poll(queue: ChunkTaskPriorityQueue<Function<Scheduler<Unit>, T>>, executor: Scheduler<T>) = scheduler.submit(PriorityTask(3) {
        val tasks = queue.pop() ?: kotlin.run {
            sleeping.add(executor)
            return@PriorityTask
        }
        tasks.map { either ->
            either.map({ left -> executor.submitFuture { left.apply(it) } }) {
                it.run()
                CompletableFuture.completedFuture(Unit)
            }
        }.toList().sequence()
    })

    @Suppress("UNCHECKED_CAST")
    private fun <T> getQueue(executor: Scheduler<T>) =
        checkNotNull(queues[executor] as? ChunkTaskPriorityQueue<Function<Scheduler<Unit>, T>>) { "No queue for $executor!" }

    class Message<T>(val position: Long, val level: () -> Int, val task: Function<Scheduler<Unit>, T>)

    class Release(val position: Long, val clearQueue: Boolean, val task: Runnable)

    companion object {

        fun message(position: Long, level: () -> Int, task: Runnable): Message<Runnable> = Message(position, level) { Runnable {
            task.run()
            it.submit(Unit)
        } }

        fun message(holder: ChunkHolder, task: Runnable) = message(holder.position.toLong(), holder::queueLevel, task)
    }
}
