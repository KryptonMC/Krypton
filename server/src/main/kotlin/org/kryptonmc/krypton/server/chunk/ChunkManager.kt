/*
 * This file is part of the Krypton project, and parts of it originate from the Paper project.
 *
 * The patch parts of this originate from is licensed under the terms of the MIT license,
 * and this file is licensed under the terms of the GNU General Public License v3.0.
 *
 * Copyright (C) PaperMC
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
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap
import org.apache.commons.lang3.mutable.MutableBoolean
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutChunkData
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateLight
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateViewPosition
import org.kryptonmc.krypton.util.PlayerAreaMap
import org.kryptonmc.krypton.util.PooledLinkedHashSets
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.progress.ChunkProgressListener
import org.kryptonmc.krypton.util.progress.ChunkStatusUpdateListener
import org.kryptonmc.krypton.util.reports.CrashReport
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
import org.kryptonmc.krypton.world.chunk.FullChunkStatus
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.chunk.ProtoChunk
import org.kryptonmc.krypton.world.chunk.ProtoKryptonChunk
import org.kryptonmc.krypton.world.chunk.data.ChunkDataManager
import org.kryptonmc.krypton.world.chunk.data.ChunkSerializer
import org.kryptonmc.krypton.world.chunk.statusAroundFull
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

class ChunkManager(
    private val world: KryptonWorld,
    private val dataAccess: WorldDataAccess,
    private val executor: Executor,
    // private val chunkCache: ChunkCache, TODO
    private val progressListener: ChunkProgressListener,
    private val statusUpdateListener: ChunkStatusUpdateListener,
    private val viewDistance: Int,
    private val syncWrites: Boolean
) : ChunkDataManager(dataAccess.dimensionPath(world.dimension).resolve("region"), syncWrites), (ChunkPosition, Boolean) -> Sequence<KryptonPlayer> {

    val chunkMap = Long2ObjectLinkedOpenHashMap<ChunkHolder>()
    private val pendingUnloads = Long2ObjectLinkedOpenHashMap<ChunkHolder>()
    private val entitiesInWorld = LongOpenHashSet()
    private val toDrop = LongOpenHashSet()
    private var modified = false
    private val queueSorter: ChunkTaskPriorityQueueSorter
    private val worldGenScheduler: Scheduler<ChunkTaskPriorityQueueSorter.Message<Runnable>>
    private val asyncScheduler: Scheduler<ChunkTaskPriorityQueueSorter.Message<Runnable>>
    private val lightScheduler = StandardScheduler.create("light updater", executor)
    val ticketManager: TicketManager = ChunkTicketManager(executor)
    private val tickingGenerated = AtomicInteger()
    private val playerMap = Object2BooleanOpenHashMap<KryptonPlayer>()
//    private val entityMap = Int2ObjectOpenHashMap<TrackedEntity>() // TODO
    private val chunkTypeCache = Long2ByteOpenHashMap()
    private val unloadQueue = ConcurrentLinkedQueue<Runnable>()

    private val pooledLinkedPlayerHashSets = PooledLinkedHashSets<KryptonPlayer>()
    private val playerBroadcastMap = PlayerAreaMap(pooledLinkedPlayerHashSets, { player, rangeX, rangeZ, currPosX, currPosZ, _, _, _ ->
        if (player.awaitingCenterUpdate) {
            player.awaitingCenterUpdate
            player.session.sendPacket(PacketOutUpdateViewPosition(currPosX, currPosZ))
        }
        updateTracking(player, rangeX, rangeZ, arrayOfNulls(2), false, true)
    }, { player, rangeX, rangeZ, _, _, _, _, _ -> updateTracking(player, rangeX, rangeZ, arrayOfNulls(2), true, false) })
    val playerDistanceMap = PlayerAreaMap(pooledLinkedPlayerHashSets, { _, rangeX, rangeZ, _, _, _, _, newState ->
        if (newState!!.size != 1) return@PlayerAreaMap
        world.chunkCache.getLoadedChunkNoCache(rangeX, rangeZ) ?: return@PlayerAreaMap
        val position = ChunkPosition(rangeX, rangeZ)
        world.chunkCache.addTicket(TicketTypes.PLAYER, position, 31, position)
    }, { _, rangeX, rangeZ, _, _, _, _, newState ->
        if (newState != null) return@PlayerAreaMap
        val position = ChunkPosition(rangeX, rangeZ)
        world.chunkCache.removeTicket(TicketTypes.PLAYER, position, 31, position)
    })

    init {
        val worldGenScheduler = StandardScheduler.create("world generation handler", executor)
        val scheduler = Scheduler.create("chunk loader", executor::execute)
        queueSorter = ChunkTaskPriorityQueueSorter(listOf(worldGenScheduler, scheduler, lightScheduler), executor, Int.MAX_VALUE)
        this.worldGenScheduler = queueSorter.getScheduler(worldGenScheduler, false)
        asyncScheduler = queueSorter.getScheduler(scheduler, false)
        ticketManager.setViewDistance(viewDistance)
    }

    fun getChunkIfPresent(position: Long): ChunkHolder? = chunkMap[position]

    fun schedule(holder: ChunkHolder, status: ChunkStatus): CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>> {
        val position = holder.position
        if (status == ChunkStatus.EMPTY) return scheduleLoad(position)
        if (status == ChunkStatus.LIGHT) ticketManager.addTicket(TicketTypes.LIGHT, position, 33 + ChunkStatus.LIGHT.distance, position)
        val access = holder.getOrScheduleFuture(status.parent, this).getNow(ChunkHolder.UNLOADED_CHUNK).left()
        return if (access.isPresent && access.get().status.isOrAfter(status)) {
            val future = status.load(world, access.get()) { protoToFull(holder) }
            progressListener.onStatusUpdate(position, status)
            future
        } else scheduleGeneration(holder, status)
    }

    fun saveAll(flush: Boolean) = if (flush) {
        val saveList = chunkMap.values.asSequence().filter { it.wasAccessibleSinceLastSave }.onEach { it.refreshAccessibility() }.toList()
        val boolean = MutableBoolean()
        do {
            boolean.setFalse()
            saveList.asSequence().map { it.toSave.get() }
                .filterNotNull()
                .filter { it is ProtoKryptonChunk || it is KryptonChunk }
                .filter { save(it) }
                .forEach { boolean.setTrue() }
        } while (boolean.isTrue)
        processUnloads(true)
    } else chunkMap.values.asSequence().filter { it.wasAccessibleSinceLastSave }.forEach {
        val chunk = it.toSave.getNow(null)
        if (chunk is ProtoKryptonChunk || chunk is KryptonChunk) {
            save(chunk)
            it.refreshAccessibility()
        }
    }

    fun prepareAccessible(holder: ChunkHolder): CompletableFuture<Either<KryptonChunk, ChunkLoadFailure>> = getChunkRangeFuture(holder.position, 1) { it.statusAroundFull() }.thenApplyAsync({ either ->
        either.mapLeft {
            // TODO: Unpack ticks
            return@mapLeft it[it.size / 2] as KryptonChunk
        }
    }) { asyncScheduler.submit(ChunkTaskPriorityQueueSorter.message(holder, it)) }

    fun prepareTicking(holder: ChunkHolder): CompletableFuture<Either<KryptonChunk, ChunkLoadFailure>> {
        val position = holder.position
        val rangeFuture = getChunkRangeFuture(position, 1) { ChunkStatus.FULL }
        val future = rangeFuture.thenApplyAsync({ either ->
            either.flatMap {
                // TODO: Post process generation
                Either.left(it[it.size / 2] as KryptonChunk)
            }
        }) { asyncScheduler.submit(ChunkTaskPriorityQueueSorter.message(holder, it)) }
        future.thenAcceptAsync({ either -> either.ifLeft { tickingGenerated.getAndIncrement() } }) { asyncScheduler.submit(ChunkTaskPriorityQueueSorter.message(holder, it)) }
        return future
    }

    fun prepareEntityTicking(position: ChunkPosition): CompletableFuture<Either<KryptonChunk, ChunkLoadFailure>> =
        getChunkRangeFuture(position, 2) { ChunkStatus.FULL }.thenApplyAsync({ either -> either.mapLeft { it[it.size / 2] as KryptonChunk } }, executor)

    fun onFullChunkStatusChange(position: ChunkPosition, status: FullChunkStatus) = statusUpdateListener.onUpdate(position, status)

    fun queueMove(player: KryptonPlayer) = executor.execute {
        val lastChunkPosition = player.lastChunkPosition
        val chunkPosition = ChunkPosition.toLong(player.location.blockX shr 4, player.location.blockZ shr 4)
        val isIgnored = playerMap.getBoolean(player)
        val shouldSkip = skipPlayer(player)
        val hasMoved = lastChunkPosition != chunkPosition
        if (hasMoved || isIgnored != shouldSkip) {
            player.lastChunkPosition = ChunkPosition.toLong(player.location.blockX shr 4, player.location.blockZ shr 4)
            if (!isIgnored) ticketManager.removePlayer(lastChunkPosition, player)
            if (!shouldSkip) ticketManager.addPlayer(chunkPosition, player)
            if (!isIgnored && shouldSkip) playerMap.replace(player, true)
            if (isIgnored && !shouldSkip) playerMap.replace(player, false)
        }
        updateMaps(player)
    }

    // TODO: Replace this with the entity tracking system
    fun trackPlayer(player: KryptonPlayer) {
        updatePlayerStatus(player, true)
    }

    fun untrackPlayer(player: KryptonPlayer) {
        updatePlayerStatus(player, false)
    }

    fun tick() = processUnloads(true)

    fun tickTracking() {
        // TODO: Update entity tracking
    }

    override fun invoke(p1: ChunkPosition, p2: Boolean) = playerMap.keys.asSequence().filter {
        val distance = p1.checkerboardDistance(it, true)
        if (distance > viewDistance) false else !p2 || distance == viewDistance
    }

    override fun close() = try {
        queueSorter.close()
    } finally {
        super.close()
    }

    private fun getChunkRangeFuture(center: ChunkPosition, radius: Int, distanceToStatus: (Int) -> ChunkStatus): CompletableFuture<Either<List<ChunkAccessor>, ChunkLoadFailure>> {
        val list = mutableListOf<CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>>>()
        val x = center.x
        val z = center.z

        for (xo in -radius..radius) {
            for (zo in -radius..radius) {
                val distance = max(abs(zo), abs(xo))
                val pos = ChunkPosition.toLong(x + xo, z + zo)
                val chunk = getChunkIfPresent(pos) ?: return CompletableFuture.completedFuture(Either.right(ChunkLoadFailure("Unloaded (${x + xo}, ${z + zo})")))
                val status = distanceToStatus(distance)
                val future = chunk.getOrScheduleFuture(status, this)
                list.add(future)
            }
        }

        return list.sequence().thenApply { eithers ->
            val accessors = mutableListOf<ChunkAccessor>()
            var count = 0
            eithers.forEach {
                val access = it.left()
                if (!access.isPresent) {
                    val currentCount = count
                    return@thenApply Either.right(ChunkLoadFailure("Unloaded (${x + currentCount % (radius * 2 + 1)}, ${z + currentCount / (radius * 2 + 1)}) ${it.right().get()}"))
                }
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
                    val read = ChunkSerializer.read(world, position, tag)
                    mark(position, read.status.type)
                    return@supplyAsync Either.left(read)
                }
                LOGGER.error("Chunk file at $position is missing required data! Skipping...")
            }
        } catch (exception: Exception) {
            LOGGER.error("Failed to load chunk at $position!", exception)
        }
        markReplaceable(position)
        Either.left(ProtoChunk(position, emptyArray(), world))
    }, executor)

    private fun mark(position: ChunkPosition, type: ChunkStatus.Type) = chunkTypeCache.put(position.toLong(), (if (type == ChunkStatus.Type.PROTO) -1 else 1).toByte())

    private fun markReplaceable(position: ChunkPosition) {
        chunkTypeCache[position.toLong()] = -1
    }

    private fun scheduleGeneration(holder: ChunkHolder, status: ChunkStatus): CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>> {
        val position = holder.position
        val future = getChunkRangeFuture(position, status.range) { getDependencyStatus(status, it) }
        val executor = Executor { worldGenScheduler.submit(ChunkTaskPriorityQueueSorter.message(holder, it)) }
        return future.thenComposeAsync({ either ->
            either.map({
                try {
                    // TODO: Generate chunk
                    progressListener.onStatusUpdate(position, status)
                    CompletableFuture.completedFuture(Either.left(it[it.size / 2]))
                } catch (exception: Exception) {
                    val report = CrashReport.of(exception, "Exception generating new chunk")
                    report.addCategory("Chunk to be generated").apply {
                        set("Location", "${position.x}, ${position.z}")
                        set("Position hash", ChunkPosition.toLong(position.x, position.z))
                        set("Generator", "None (not supported yet)")
                    }
                    throw ReportedException(report)
                }
            }, {
                // TODO: Release light ticket
                CompletableFuture.completedFuture(Either.right(it))
            })
        }, executor)
    }

    private fun scheduleUnload(position: Long, holder: ChunkHolder) {
        val toSave = holder.toSave
        val callback = { chunk: ChunkAccessor? ->
            val future = holder.toSave
            if (future != toSave) {
                scheduleUnload(position, holder)
            } else {
                if (pendingUnloads.remove(key = position, value = holder) && chunk != null) {
                    if (chunk is KryptonChunk) chunk.isLoaded = false
                    save(chunk)
                    entitiesInWorld.remove(position)
                    // TODO: Light engine and unloading for the world, to invalidate block entities
                    progressListener.onStatusUpdate(chunk.position, null)
                }
            }
        }
        toSave.thenAcceptAsync(callback, unloadQueue::add).whenComplete { _, exception ->
            if (exception != null) LOGGER.error("Failed to save chunk at (${holder.position.x}, ${holder.position.z})!", exception)
        }
    }

    private fun protoToFull(holder: ChunkHolder): CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>> = holder.getFutureIfPresentUnchecked(ChunkStatus.FULL.parent).thenApplyAsync({ either ->
        val status = holder.ticketLevel.toChunkStatus()
        if (!status.isOrAfter(ChunkStatus.FULL)) ChunkHolder.UNLOADED_CHUNK else either.mapLeft {
            val proto = it as ProtoChunk
            val full = if (proto is ProtoKryptonChunk) proto.wrapped else KryptonChunk(world, proto).apply {
                holder.replaceProto(ProtoKryptonChunk(this))
            }
            full.fullStatus = { holder.ticketLevel.toFullStatus() }
            if (entitiesInWorld.add(it.position.toLong())) {
                full.isLoaded = true
                // TODO: Register block entities
            }
            full
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
            chunkMap[position] = temp
            modified = true
        }
        return temp
    }

    private fun updateTracking(player: KryptonPlayer, x: Int, z: Int, packets: Array<Packet?>, inMaxWatch: Boolean, inView: Boolean) {
        if (player.world != world) return
        if (inView && !inMaxWatch) getChunkIfPresent(ChunkPosition.toLong(x, z))?.let { holder ->
            holder.tickingChunk?.let { loadPlayerChunk(player, packets, it) }
        }
        if (!inView && inMaxWatch) player.untrackChunk(x, z)
    }

    private fun loadPlayerChunk(player: KryptonPlayer, packets: Array<Packet?>, chunk: KryptonChunk) {
        if (packets[0] == null) {
            packets[0] = PacketOutChunkData(chunk)
            packets[1] = PacketOutUpdateLight(chunk) // TODO: Fix lighting when the engine is added
        }
        player.trackChunk(packets[0]!!, packets[1]!!)
        // TODO: Update entity tracking
    }

    private fun readChunk(position: ChunkPosition): CompoundTag? = read(position)?.apply { upgrade(world.dimension, this, position, world) }

    private fun save(chunk: ChunkAccessor): Boolean {
        if (!chunk.isUnsaved) return false
        chunk.isUnsaved = true
        val position = chunk.position
        return try {
            write(position, ChunkSerializer.write(world, chunk))
            mark(position, chunk.status.type)
            true
        } catch (exception: Exception) {
            LOGGER.error("Failed to save chunk at (${position.x}, ${position.z})!", exception)
            false
        }
    }

    private fun processUnloads(shouldKeepTicking: Boolean) {
        val iterator = toDrop.iterator()
        var i = 0
        while (iterator.hasNext() && (shouldKeepTicking || i < 200 || toDrop.size > 2000)) {
            val next = iterator.nextLong()
            val chunk = chunkMap.remove(next)
            if (chunk != null) {
                pendingUnloads[next] = chunk
                modified = true
                ++i
                scheduleUnload(next, chunk)
            }
            iterator.remove()
        }
        var task = unloadQueue.poll()
        while ((shouldKeepTicking || unloadQueue.size > 2000) && task != null) {
            task.run()
            task = unloadQueue.poll()
        }
    }

    private fun updatePlayerStatus(player: KryptonPlayer, added: Boolean) {
        val shouldSkip = skipPlayer(player)
        val isIgnored = playerMap.getOrDefault(key = player, true)
        val chunkX = player.location.blockX shr 4
        val chunkZ = player.location.blockZ shr 4
        val lastChunkPosition = player.lastChunkPosition
        val currentChunkPosition = ChunkPosition.toLong(chunkX, chunkZ)
        if (added) {
            playerMap[player] = shouldSkip
            player.lastChunkPosition = currentChunkPosition
            if (!shouldSkip) ticketManager.addPlayer(currentChunkPosition, player)
            addPlayerToMaps(player)
        } else {
            playerMap.removeBoolean(player)
            if (!isIgnored) ticketManager.removePlayer(lastChunkPosition, player)
            removePlayerFromMaps(player)
        }
    }

    private fun addPlayerToMaps(player: KryptonPlayer) {
        val chunkX = player.location.blockX shr 4
        val chunkZ = player.location.blockZ shr 4
        playerDistanceMap.update(player, chunkX, chunkZ, TicketManager.MOB_SPAWN_RANGE)
        playerDistanceMap.add(player, chunkX, chunkZ, TicketManager.MOB_SPAWN_RANGE)
        val effectiveViewDistance = viewDistance - 1
        if (!skipPlayer(player)) playerDistanceMap.add(player, chunkX, chunkZ, effectiveViewDistance)
        player.awaitingCenterUpdate = true
        playerBroadcastMap.add(player, chunkX, chunkZ, effectiveViewDistance)
        player.awaitingCenterUpdate = false
    }

    private fun removePlayerFromMaps(player: KryptonPlayer) {
        playerBroadcastMap.remove(player)
        playerDistanceMap.remove(player)
    }

    private fun updateMaps(player: KryptonPlayer) {
        val chunkX = player.location.blockX shr 4
        val chunkZ = player.location.blockZ shr 4
        playerDistanceMap.update(player, chunkX, chunkZ, TicketManager.MOB_SPAWN_RANGE)
        val effectiveViewDistance = viewDistance - 1
        if (!skipPlayer(player)) playerDistanceMap.update(player, chunkX, chunkZ, effectiveViewDistance)
        player.awaitingCenterUpdate = true
        playerBroadcastMap.update(player, chunkX, chunkZ, effectiveViewDistance)
        player.awaitingCenterUpdate = false
    }

    private fun skipPlayer(player: KryptonPlayer) = player.gamemode == Gamemode.SPECTATOR && !world.data.gameRules[GameRules.SPECTATORS_GENERATE_CHUNKS]

    private fun getDependencyStatus(status: ChunkStatus, id: Int): ChunkStatus = if (id == 0) status.parent else (status.distance + id).statusAroundFull()

    private inner class ChunkTicketManager(executor: Executor) : TicketManager(executor, this@ChunkManager) {

        override fun getChunk(position: Long) = getChunkIfPresent(position)

        override fun isChunkToRemove(position: Long) = toDrop.contains(position)

        override fun updateChunkScheduling(position: Long, level: Int, holder: ChunkHolder?, oldLevel: Int) =
            this@ChunkManager.updateChunkScheduling(position, level, holder, oldLevel)
    }

    companion object {

        private const val CHUNK_TYPE_REPLACEABLE = -1
        private const val CHUNK_TYPE_UNKNOWN = 0
        private const val CHUNK_TYPE_FULL = 1
        private const val CHUNK_SAVED_PER_TICK = 200
        private const val MIN_VIEW_DISTANCE = 3
        private const val TWO_THIRD_MODIFIER = 2.0 / 3.0
        const val MAX_VIEW_DISTANCE = 33
        val MAX_CHUNK_DISTANCE = 33 + ChunkStatus.MAX_DISTANCE
        private val LOGGER = logger<ChunkManager>()
    }
}

private fun Long2IntOpenHashMap.update(chunk: Long, level: Int) {
    val prev = getOrDefault(chunk, -1)
    if (level > prev) this[chunk] = level
}
