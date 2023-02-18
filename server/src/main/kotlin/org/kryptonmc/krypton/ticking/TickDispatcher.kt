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
package org.kryptonmc.krypton.ticking

import org.jctools.queues.MpscUnboundedArrayQueue
import org.kryptonmc.krypton.util.ImmutableLists
import java.util.ArrayDeque
import java.util.WeakHashMap
import java.util.concurrent.CountDownLatch
import kotlin.math.abs

/**
 * A dispatcher for tick threads that tick tickable elements in partitions.
 *
 * A partition is simply a region that contains tickable elements, such as a
 * chunk. This dispatcher will assign threads dependent on the policy of the
 * provider and the number of threads that are available, allowing for
 * multi-threaded ticking of elements.
 *
 * Not all elements are ticked using this system. The main server tick, which
 * includes the scheduler process, the world tick, and the connection tick,
 * are all still single-threaded, as they do not need to scale up to the same
 * amounts that chunks and entities do.
 *
 * This design is heavily taken from Minestom, with some modifications to
 * remove acquirables, and all that which is required for acquirables to work.
 */
class TickDispatcher<P : Any>(private val provider: TickThreadProvider<P>, threadCount: Int) {

    private val threads: List<TickThread> = ImmutableLists.ofArray(Array(threadCount) { TickThread(it) })

    // Defines how computation is dispatched to the available threads
    private val partitions = WeakHashMap<P, DispatchContext>()
    // Cache to retrieve the threading context from a tickable element
    private val elements = WeakHashMap<Tickable, DispatchContext>()
    // Queue of partitions to update. This is filled with loaded and unloaded partitions.
    private val partitionUpdateQueue = ArrayDeque<P>()

    // Pending updates that will be applied on the next tick
    private val updates = MpscUnboundedArrayQueue<DispatchUpdate<P>>(1024)

    init {
        threads.forEach { it.start() }
    }

    /**
     * Processes all available updates and waits for all threads to finish
     * their tick process.
     *
     * @param time the start time, in milliseconds
     */
    @Synchronized
    fun updateAndAwait(time: Long) {
        // Process all waiting updates
        updates.drain { update ->
            when (update) {
                is DispatchUpdate.PartitionLoad -> loadPartition(update.partition)
                is DispatchUpdate.PartitionUnload -> unloadPartition(update.partition)
                is DispatchUpdate.ElementUpdate -> updateElement(update.tickable, update.partition)
                is DispatchUpdate.ElementRemove -> removeElement(update.tickable)
            }
        }

        // Start the tick and wait for all threads that start ticking to finish
        val latch = CountDownLatch(threads.size)
        threads.forEach { it.startTick(latch, time) }
        latch.await()
    }

    /**
     * Refreshes the threads that are assigned to each partition.
     *
     * The way the system is designed, the thread that we want may change when
     * new chunks are added or removed, so we can better load balance across
     * the threads. This method implements this behaviour, checking the thread
     * that a partition should be allocated to, and updating it if needed.
     *
     * @param nanoTimeout the time, in nanoseconds, until the refresh should
     * stop, which will prevent it from running for too long
     */
    fun refreshThreads(nanoTimeout: Long) {
        val currentTime = System.nanoTime()
        var counter = partitionUpdateQueue.size

        while (true) {
            val partition = partitionUpdateQueue.pollFirst() ?: break
            val context = partitions.get(partition)!!
            val previous = context.thread
            val next = findThread(partition)

            if (next !== previous) {
                context.thread = next
                previous.entries().remove(context)
                next.entries().add(context)
            }

            partitionUpdateQueue.addLast(partition)
            if (--counter <= 0 || System.nanoTime() - currentTime >= nanoTimeout) break
        }
    }

    fun shutdown() {
        threads.forEach { it.shutdown() }
    }

    /**
     * Signals to the dispatcher that a new partition was loaded, and that it
     * should allocate it to a thread.
     */
    fun queuePartitionLoad(partition: P) {
        queueUpdate(DispatchUpdate.PartitionLoad(partition))
    }

    /**
     * Signals to the dispatcher that a partition was unloaded, and that it
     * should deallocate it.
     */
    fun queuePartitionUnload(partition: P) {
        queueUpdate(DispatchUpdate.PartitionUnload(partition))
    }

    /**
     * Signals to the dispatcher that a tickable element was moved to a
     * partition.
     *
     * This can be used when a tickable element is added to a partition and
     * when it is moved to a different partition.
     */
    fun queueElementUpdate(tickable: Tickable, partition: P) {
        queueUpdate(DispatchUpdate.ElementUpdate(tickable, partition))
    }

    /**
     * Signals to the dispatcher that a tickable element was removed, and it
     * should remove it from the partition it is in, if any, and no longer
     * tick it.
     */
    fun queueElementRemove(tickable: Tickable) {
        queueUpdate(DispatchUpdate.ElementRemove(tickable))
    }

    private fun queueUpdate(update: DispatchUpdate<P>) {
        updates.relaxedOffer(update)
    }

    private fun findThread(partition: P): TickThread {
        val threadId = provider.findThread(partition)
        val index = abs(threadId) % threads.size
        return threads.get(index)
    }

    private fun loadPartition(partition: P) {
        if (partitions.containsKey(partition)) return

        val thread = findThread(partition)
        val context = DispatchContext(thread)
        thread.entries().add(context)

        partitions.put(partition, context)
        partitionUpdateQueue.add(partition)
        if (partition is Tickable) updateElement(partition, partition)
    }

    private fun unloadPartition(partition: P) {
        val context = partitions.remove(partition)
        if (context != null) {
            val thread = context.thread
            thread.entries().remove(context)
        }

        partitionUpdateQueue.remove(partition)
        if (partition is Tickable) removeElement(partition)
    }

    private fun updateElement(tickable: Tickable, partition: P) {
        var context = elements.get(tickable)
        @Suppress("IfThenToSafeAccess") // This is clearer
        if (context != null) context.elements.remove(tickable)

        context = partitions.get(partition)
        if (context != null) {
            elements.put(tickable, context)
            context.elements.add(tickable)
        }
    }

    private fun removeElement(tickable: Tickable) {
        val partition = elements.get(tickable)
        @Suppress("IfThenToSafeAccess") // This is clearer
        if (partition != null) partition.elements.remove(tickable)
    }

    class DispatchContext(var thread: TickThread) {

        val elements: MutableList<Tickable> = ArrayList()
    }

    private sealed interface DispatchUpdate<P> {

        @JvmRecord
        data class PartitionLoad<P>(val partition: P) : DispatchUpdate<P>

        @JvmRecord
        data class PartitionUnload<P>(val partition: P) : DispatchUpdate<P>

        @JvmRecord
        data class ElementUpdate<P>(val tickable: Tickable, val partition: P) : DispatchUpdate<P>

        @JvmRecord
        data class ElementRemove<P>(val tickable: Tickable) : DispatchUpdate<P>
    }
}
