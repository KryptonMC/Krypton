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
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.reports.ReportedException
import org.kryptonmc.krypton.util.sequence
import org.kryptonmc.krypton.util.thread.Scheduler
import org.kryptonmc.krypton.util.thread.StandardScheduler
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import org.kryptonmc.krypton.world.chunk.ChunkHolder
import org.kryptonmc.krypton.world.chunk.ChunkLoadFailure
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.chunk.ProtoChunk
import org.kryptonmc.krypton.world.chunk.ProtoKryptonChunk
import org.kryptonmc.krypton.world.chunk.data.ChunkDataManager
import org.kryptonmc.krypton.world.chunk.data.ChunkReader
import org.kryptonmc.krypton.world.chunk.ticket.TicketTypes
import org.kryptonmc.krypton.world.chunk.toChunkStatus
import org.kryptonmc.krypton.world.chunk.toFullStatus
import org.kryptonmc.krypton.world.storage.WorldDataAccess
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs
import kotlin.math.max

class ChunkMap(
    private val world: KryptonWorld,
    private val dataAccess: WorldDataAccess,
    private val executor: Executor,
    private val viewDistance: Int,
    private val syncWrites: Boolean
) : ChunkDataManager(dataAccess.dimensionPath(world.dimension).resolve("region"), syncWrites), (ChunkPosition, Boolean) -> Sequence<KryptonPlayer> {

    private val updatingChunkMap = Long2ObjectLinkedOpenHashMap<ChunkHolder>()
    @Volatile private var visibleChunkMap = updatingChunkMap.clone()
    private val pendingUnloads = Long2ObjectLinkedOpenHashMap<ChunkHolder>()
    private val entities = LongOpenHashSet()
    private val toDrop = LongOpenHashSet()
    private var modified = false
    private val queueSorter: ChunkTaskPriorityQueueSorter
    private val worldGenScheduler: Scheduler<ChunkTaskPriorityQueueSorter.Message<Runnable>>
    private val asyncScheduler: Scheduler<ChunkTaskPriorityQueueSorter.Message<Runnable>>
    private val lightScheduler = StandardScheduler.create("light updater", executor)
    private val ticketManager = ChunkTicketManager(executor)
    private val tickingGenerated = AtomicInteger()
    private val playerMap = Object2BooleanOpenHashMap<KryptonPlayer>()
//    private val entityMap = Int2ObjectOpenHashMap<TrackedEntity>() // TODO
    private val chunkTypeCache = Long2ByteOpenHashMap()
    private val unloadQueue = ConcurrentLinkedQueue<Runnable>()

    init {
        val worldGenScheduler = StandardScheduler.create("world generation handler", executor)
        val scheduler = Scheduler.create("chunk loader", executor::execute)
        queueSorter = ChunkTaskPriorityQueueSorter(listOf(worldGenScheduler, scheduler, lightScheduler), executor, Int.MAX_VALUE)
        this.worldGenScheduler = queueSorter.getScheduler(worldGenScheduler, false)
        asyncScheduler = queueSorter.getScheduler(scheduler, false)
    }

    fun getUpdatingChunkIfPresent(position: Long): ChunkHolder? = updatingChunkMap[position]

    fun getVisibleChunkIfPresent(position: Long): ChunkHolder? = visibleChunkMap[position]

    fun schedule(holder: ChunkHolder, status: ChunkStatus): CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>> {
        val position = holder.position
        return scheduleLoad(position)
        // TODO: Add generation and fix this
//        if (status == ChunkStatus.EMPTY) return scheduleLoad(position)
//        if (status == ChunkStatus.LIGHT) ticketManager.addTicket(TicketTypes.LIGHT, position, 33 + ChunkStatus.LIGHT.distance, position)
//        val access = holder.getOrScheduleFuture(status.parent, this).getNow(ChunkHolder.UNLOADED_CHUNK).left()
//        if (access.isPresent) return
    }

    fun promote(): Boolean {
        if (!modified) return false
        visibleChunkMap = updatingChunkMap.clone()
        modified = false
        return true
    }

    override fun invoke(p1: ChunkPosition, p2: Boolean) = playerMap.keys.asSequence().filter {
        val distance = p1.checkerboardDistance(it, true)
        if (distance > viewDistance) false else !p2 || distance == viewDistance
    }

    private fun getChunkRangeFuture(center: ChunkPosition, margin: Int, distanceToStatus: (Int) -> ChunkStatus): CompletableFuture<Either<List<ChunkAccessor>, ChunkLoadFailure>> {
        val list = mutableListOf<CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>>>()
        val x = center.x
        val z = center.z

        for (xo in -margin..margin) {
            for (zo in -margin..margin) {
                val distance = max(abs(zo), abs(xo))
                val pos = ChunkPosition.toLong(x + xo, z + zo)
                val chunk = getUpdatingChunkIfPresent(pos) ?: return CompletableFuture.completedFuture(Either.right(ChunkLoadFailure("Unloaded (${x + xo}, ${z + zo})")))
                val status = distanceToStatus(distance)
                val future = chunk.getOrScheduleFuture(status, this)
                list.add(future)
            }
        }
        return list.sequence().thenApply {
            val accessors = mutableListOf<ChunkAccessor>()
            val iterator = it.iterator()
            var count = 0
            while (iterator.hasNext()) {
                val currentCount = count
                val next = iterator.next()
                val access = next.left()
                if (!access.isPresent) return@thenApply Either.right(ChunkLoadFailure("Unloaded ($x, $z) ${next.right().get()}"))
                accessors.add(access.get())
                ++count
            }
            Either.left(accessors)
        }
    }

    private fun scheduleLoad(position: ChunkPosition): CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>> = CompletableFuture.supplyAsync({
        try {
            val tag = readChunk(position)
            if (tag != null) {
                val hasStatus = tag.contains("Level", CompoundTag.ID) && tag.getCompound("Level").contains("Status", StringTag.ID)
                if (hasStatus) {
                    val read = ChunkReader.read(world, position, tag)
                    chunkTypeCache[position.toLong()] = (if (read.status.type == ChunkStatus.Type.PROTO) CHUNK_TYPE_REPLACEABLE else CHUNK_TYPE_FULL).toByte()
                    return@supplyAsync Either.left(read)
                }
                LOGGER.error("Chunk file at $position is missing required data! Skipping...")
            }
            val hasStatus = tag
        } catch (exception: Exception) {
            LOGGER.error("Failed to load chunk at $position!", exception)
        }
        chunkTypeCache[position.toLong()] = -1
        Either.left(ProtoChunk(position, emptyArray(), world))
    }, executor)

    private fun protoToFull(holder: ChunkHolder) = holder.getFutureIfPresentUnchecked(ChunkStatus.FULL.parent).thenApplyAsync({ either ->
        val status = holder.ticketLevel.toChunkStatus()
        if (!status.isOrAfter(ChunkStatus.FULL)) ChunkHolder.UNLOADED_CHUNK else either.mapLeft {
            val proto = it as ProtoChunk
            (if (proto is ProtoKryptonChunk) proto.wrapped else KryptonChunk(world, proto)).apply { fullStatus = { holder.ticketLevel.toFullStatus() } }
        }
    }) { asyncScheduler.submit(ChunkTaskPriorityQueueSorter.message(holder.position.toLong(), { 1 }, it)) }

    private fun updateChunkScheduling(position: Long, level: Int, holder: ChunkHolder?, oldLevel: Int): ChunkHolder? {
        if (oldLevel > MAX_CHUNK_DISTANCE && level > MAX_CHUNK_DISTANCE) return holder
        holder?.ticketLevel = level
        if (holder != null) if (level > MAX_CHUNK_DISTANCE) toDrop.add(position) else toDrop.remove(position)
        var temp = holder
        if (level <= MAX_CHUNK_DISTANCE && holder == null) {
            temp = pendingUnloads.remove(position) ?: ChunkHolder(ChunkPosition(position), level, world, queueSorter, this)
            temp.ticketLevel = level
            updatingChunkMap[position] = temp
            modified = true
        }
        return temp
    }

    private fun readChunk(position: ChunkPosition): CompoundTag? = read(position)?.apply { upgrade(world.dimension, this, position, world) }

    private inner class ChunkTicketManager(executor: Executor) : TicketManager(executor, viewDistance) {

        override fun getChunk(position: Long) = getUpdatingChunkIfPresent(position)

        override fun isChunkToRemove(position: Long) = toDrop.contains(position)

        override fun updateChunkScheduling(position: Long, level: Int, holder: ChunkHolder?, oldLevel: Int) =
            this@ChunkMap.updateChunkScheduling(position, level, holder, oldLevel)
    }

    companion object {

        private const val CHUNK_TYPE_REPLACEABLE = -1
        private const val CHUNK_TYPE_UNKNOWN = 0
        private const val CHUNK_TYPE_FULL = 1
        private const val CHUNK_SAVED_PER_TICK = 200
        private const val MIN_VIEW_DISTANCE = 3
        const val MAX_VIEW_DISTANCE = 33
        val MAX_CHUNK_DISTANCE = 33 + ChunkStatus.MAX_DISTANCE
        private val LOGGER = logger<ChunkMap>()
    }
}
