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

import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap
import it.unimi.dsi.fastutil.longs.Long2IntMaps
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.SortedArraySet
import org.kryptonmc.krypton.util.thread.Scheduler
import org.kryptonmc.krypton.world.chunk.ChunkHolder
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import org.kryptonmc.krypton.world.chunk.ticket.ChunkTicket
import org.kryptonmc.krypton.world.chunk.ticket.TicketType
import org.kryptonmc.krypton.world.chunk.ticket.TicketTypes
import java.util.ArrayDeque
import java.util.concurrent.Executor

abstract class TicketManager(
    private val executor: Executor,
    private val chunkManager: ChunkManager
) {

    private val playersByChunk = Long2ObjectOpenHashMap<ObjectSet<KryptonPlayer>>()
    private val tickets = Long2ObjectOpenHashMap<SortedArraySet<ChunkTicket<*>>>()
    private val ticketTracker = ChunkTicketTracker()
    private val playerTicketManager = PlayerTicketTracker(PLAYER_TICKET_LEVEL)
    private val pendingChunkUpdates = object : ArrayDeque<ChunkHolder>() {
        override fun add(element: ChunkHolder): Boolean {
            if (element.isUpdateQueued) return true
            element.isUpdateQueued = true
            return super.add(element)
        }
    }
    private var pollingPendingChunkUpdates = false
    private val ticketThrottler: ChunkTaskPriorityQueueSorter
    private val ticketThrottlerInput: Scheduler<ChunkTaskPriorityQueueSorter.Message<Runnable>>
    private val ticketThrottlerReleaser: Scheduler<ChunkTaskPriorityQueueSorter.Release>
    private val pendingReleases = LongOpenHashSet()
    private var tickCount = 0L

    val naturalSpawnChunkCount: Int
        get() = chunkManager.playerDistanceMap.size

    init {
        val throttler = Scheduler.create<Runnable>("player ticket throttler", executor::execute)
        val sorter = ChunkTaskPriorityQueueSorter(listOf(throttler), executor, 4)
        ticketThrottler = sorter
        ticketThrottlerInput = sorter.getScheduler(throttler, true)
        ticketThrottlerReleaser = sorter.getReleaseScheduler(throttler)
    }

    protected abstract fun isChunkToRemove(position: Long): Boolean

    protected abstract fun getChunk(position: Long): ChunkHolder?

    protected abstract fun updateChunkScheduling(position: Long, level: Int, holder: ChunkHolder?, oldLevel: Int): ChunkHolder?

    fun addPlayer(position: Long, player: KryptonPlayer) {
        playersByChunk.getOrPut(position) { ObjectOpenHashSet() }.add(player)
        playerTicketManager.update(position, 0, true)
    }

    fun removePlayer(position: Long, player: KryptonPlayer) {
        val list = playersByChunk[position]?.apply { remove(player) } ?: return
        if (list.isEmpty()) {
            playersByChunk.remove(position)
            playerTicketManager.update(position, Int.MAX_VALUE, false)
        }
    }

    fun hasPlayersNearby(position: Long) = chunkManager.playerDistanceMap.inRange(position) != null

    fun <T> addTicket(type: TicketType<T>, position: ChunkPosition, level: Int, key: T) = addTicket(position.toLong(), ChunkTicket(type, level, key))

    fun <T> removeTicket(type: TicketType<T>, position: ChunkPosition, level: Int, key: T) = removeTicket(position.toLong(), ChunkTicket(type, level, key))

    fun <T> addRegionTicket(type: TicketType<T>, position: ChunkPosition, radius: Int, key: T) = addTicket(position.toLong(), ChunkTicket(type, PLAYER_TICKET_LEVEL - radius, key))

    fun <T> removeRegionTicket(type: TicketType<T>, position: ChunkPosition, radius: Int, key: T) = removeTicket(position.toLong(), ChunkTicket(type, PLAYER_TICKET_LEVEL - radius, key))

    fun purge() {
        tickCount++
        val iterator = tickets.long2ObjectEntrySet().fastIterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.value.removeIf { it.hasTimedOut(tickCount) }) ticketTracker.update(next.longKey, next.value.level(), false)
            if (next.value.isEmpty()) iterator.remove()
        }
    }

    fun update(): Boolean {
        playerTicketManager.runUpdates()
        val hasUpdates = Int.MAX_VALUE - ticketTracker.update(Int.MAX_VALUE) != 0
        if (!pendingChunkUpdates.isEmpty()) {
            while (!pendingChunkUpdates.isEmpty()) {
                val remove = pendingChunkUpdates.remove()
                remove.isUpdateQueued = false
                remove.updateFutures(chunkManager, executor)
            }
            return true
        }
        if (pendingReleases.isEmpty()) return hasUpdates
        val iterator = pendingReleases.iterator()
        while (iterator.hasNext()) {
            val next = iterator.nextLong()
            if (!tickets.getOrPut(next) { SortedArraySet.create(4) }.any { it.type === TicketTypes.PLAYER }) continue
            val chunk = chunkManager.getChunkIfPresent(next) ?: error("")
            chunk.entityTickingFuture.thenAccept {
                executor.execute { ticketThrottlerReleaser.submit(ChunkTaskPriorityQueueSorter.Release(next, false) {}) }
            }
        }
        pendingReleases.clear()
        return hasUpdates
    }

    fun setViewDistance(distance: Int) = playerTicketManager.updateViewDistance(distance)

    private fun addTicket(position: Long, ticket: ChunkTicket<*>) {
        val list = tickets.getOrPut(position) { SortedArraySet.create(4) }
        val level = list.level()
        list.addOrGet(ticket)?.apply { createdTick = tickCount }
        if (ticket.level < level) ticketTracker.update(position, ticket.level, true)
    }

    private fun removeTicket(position: Long, ticket: ChunkTicket<*>) {
        val list = tickets.getOrPut(position) { SortedArraySet.create(4) }
        val oldLevel = list.level()
        if (list.remove(ticket) && ticket.type == TicketTypes.PLAYER) {
            var hasPlayer = false
            for (element in list) {
                if (element.type !== TicketTypes.PLAYER) continue
                hasPlayer = true
                break
            }
            val playerChunk = chunkManager.getChunkIfPresent(position)
            if (!hasPlayer && playerChunk != null && playerChunk.isFullChunkReady) {
                val delayUnload = ChunkTicket(TicketTypes.DELAYED_UNLOAD, 33, position).apply {
                    delayUnloadBy = DELAY_UNLOADS_BY
                    createdTick = tickCount
                }
                list.remove(delayUnload)
                list.add(delayUnload)
            }
        }
        if (list.isEmpty()) tickets.remove(position)
        val newLevel = list.level()
        if (newLevel > oldLevel) ticketTracker.update(position, list.level(), false)
    }

    private inner class ChunkTicketTracker : ChunkTracker(ChunkManager.MAX_CHUNK_DISTANCE + 2, 16, 256) {

        fun update(distance: Int) = runUpdates(distance)

        override fun getLevelFromSource(id: Long): Int {
            val list = tickets[id] ?: return Int.MAX_VALUE
            return if (list.isEmpty()) Int.MAX_VALUE else list.first!!.level
        }

        override fun getLevel(id: Long): Int {
            if (!isChunkToRemove(id)) getChunk(id)?.let { return it.ticketLevel }
            return ChunkManager.MAX_CHUNK_DISTANCE + 1
        }

        override fun setLevel(id: Long, level: Int) {
            var holder = getChunk(id)
            val ticketLevel = holder?.ticketLevel ?: ChunkManager.MAX_CHUNK_DISTANCE + 1
            if (ticketLevel != level) {
                holder = updateChunkScheduling(id, level, holder, ticketLevel)
                if (holder != null) pendingChunkUpdates.add(holder)
            }
        }
    }

    private open inner class FixedPlayerDistanceChunkTracker(private val maxDistance: Int) : ChunkTracker(maxDistance + 2, 16, 256) {

        val chunks = Long2ByteOpenHashMap().apply { defaultReturnValue((maxDistance + 2).toByte()) }

        fun runUpdates() = runUpdates(Int.MAX_VALUE)

        protected open fun onLevelChange(position: Long, oldLevel: Int, newLevel: Int) {}

        override fun getLevelFromSource(id: Long) = if (havePlayer(id)) 0 else Int.MAX_VALUE

        override fun getLevel(id: Long) = chunks[id].toInt()

        override fun setLevel(id: Long, level: Int) {
            val old = if (level > maxDistance) chunks.remove(id) else chunks.put(id, level.toByte())
            onLevelChange(id, old.toInt(), level)
        }

        private fun havePlayer(position: Long): Boolean {
            val list = playersByChunk[position]
            return list != null && list.isNotEmpty()
        }
    }

    private inner class PlayerTicketTracker(maxDistance: Int) : FixedPlayerDistanceChunkTracker(maxDistance) {

        private var viewDistance = 0
        val queueLevels = Long2IntMaps.synchronize(Long2IntOpenHashMap()).apply { defaultReturnValue(maxDistance + 2) }
        val toUpdate = LongOpenHashSet()

        override fun onLevelChange(position: Long, oldLevel: Int, newLevel: Int) {
            toUpdate.add(position)
        }

        fun updateViewDistance(viewDistance: Int) {
            val iterator = chunks.long2ByteEntrySet().iterator()
            val lastViewDistance = this.viewDistance
            this.viewDistance = viewDistance
            while (iterator.hasNext()) {
                val next = iterator.next()
                val key = next.longKey
                val value = next.byteValue.toInt()
                onLevelChange(key, value, value <= lastViewDistance - 2, haveTicketFor(value))
            }
        }

        private fun onLevelChange(position: Long, level: Int, previouslyInView: Boolean, inView: Boolean) {
            if (previouslyInView == inView) return
            val ticket = ChunkTicket(TicketTypes.PLAYER, PLAYER_TICKET_LEVEL, ChunkPosition(position))
            if (inView) ticketThrottlerInput.submit(ChunkTaskPriorityQueueSorter.message(position, { level }) {
                if (haveTicketFor(getLevel(position))) {
                    addTicket(position, ticket)
                    pendingReleases.add(position)
                } else {
                    ticketThrottlerReleaser.submit(ChunkTaskPriorityQueueSorter.Release(position, false) {})
                }
            }) else ticketThrottlerReleaser.submit(ChunkTaskPriorityQueueSorter.Release(position, true) {
                executor.execute { removeTicket(position, ticket) }
            })
        }

        private fun haveTicketFor(level: Int) = level <= viewDistance - 2
    }

    companion object {

        private const val ENTITY_TICKING_RANGE = 2
        private const val INITIAL_TICKET_LIST_CAPACITY = 4
        const val MOB_SPAWN_RANGE = 8
        private const val DELAY_UNLOADS_BY = 20L
        private val PLAYER_TICKET_LEVEL = 33 + ChunkStatus.FULL.distance - 2
    }
}

private fun SortedArraySet<ChunkTicket<*>>.level(): Int = if (isNotEmpty()) first!!.level else ChunkManager.MAX_CHUNK_DISTANCE + 1
