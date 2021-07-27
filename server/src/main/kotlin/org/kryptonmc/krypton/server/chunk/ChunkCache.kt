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
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.CHUNK_SCHEDULER
import org.kryptonmc.krypton.util.progress.ChunkProgressListener
import org.kryptonmc.krypton.util.progress.ChunkStatusUpdateListener
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import org.kryptonmc.krypton.world.chunk.ChunkHolder
import org.kryptonmc.krypton.world.chunk.ChunkLoadFailure
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import org.kryptonmc.krypton.world.chunk.FullChunkStatus
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.chunk.ProtoKryptonChunk
import org.kryptonmc.krypton.world.chunk.ticket.TicketType
import org.kryptonmc.krypton.world.chunk.ticket.TicketTypes
import org.kryptonmc.krypton.world.chunk.toFullStatus
import org.kryptonmc.krypton.world.storage.WorldDataAccess
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class ChunkCache(
    private val world: KryptonWorld,
    private val dataAccess: WorldDataAccess,
    private val executor: Executor,
    private val progressListener: ChunkProgressListener,
    private val statusUpdateListener: ChunkStatusUpdateListener,
    private val viewDistance: Int,
    private val syncWrites: Boolean
) : AutoCloseable {

    val chunkManager = ChunkManager(world, dataAccess, executor, progressListener, statusUpdateListener, viewDistance, syncWrites)
    private val ticketManager: TicketManager = chunkManager.ticketManager
    val loadedChunkMap: Long2ObjectMap<KryptonChunk> = Long2ObjectMaps.synchronize(Long2ObjectOpenHashMap(8192, 0.5F))
    private var lastChunkPos = LongArray(4)
    private val lastChunkStatus = arrayOfNulls<ChunkStatus>(4)
    private val lastChunk = arrayOfNulls<ChunkAccessor>(4)
    private val lastLoadedChunks = arrayOfNulls<KryptonChunk>(4 * 4)
    private var futureAwaitCounter = 0L
    private var asyncLoadCounter = 0L
    private var lastInhabitedUpdate = 0L

    fun init(): ScheduledFuture<*> = CHUNK_SCHEDULER.scheduleAtFixedRate({
        ticketManager.purge()
        updateTicketManager()
        tickChunks()
        chunkManager.tick()
        clearCache()
    }, 0, 50, TimeUnit.MILLISECONDS)

    operator fun get(x: Int, z: Int): CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>> {
        val key = ChunkPosition.toLong(x, z)
        val position = ChunkPosition(x, z)
        val chunk: ChunkAccessor
        for (i in 0 until 4) {
            if (key != lastChunkPos[i] || lastChunkStatus[i] !== ChunkStatus.FULL) continue
            chunk = lastChunk[i] ?: continue
            for (j in 3 downTo 1) {
                lastChunkPos[j] = lastChunkPos[j - 1]
                lastChunkStatus[j] = lastChunkStatus[j - 1]
                lastChunk[j] = lastChunk[j - 1]
            }
            lastChunkPos[0] = key
            lastChunkStatus[0] = ChunkStatus.FULL
            lastChunk[0] = chunk
            return CompletableFuture.completedFuture(Either.left(chunk))
        }
        getNow(x, z)?.let {
            if (it !is ProtoKryptonChunk && it !is KryptonChunk) return CompletableFuture.completedFuture(ChunkHolder.UNLOADED_CHUNK)
            return bringToStatus(x, z, position, ChunkStatus.FULL)
        }
        return bringToStatus(x, z, position, ChunkStatus.EMPTY).thenCompose {
            val chunk = it.left().orElse(null)
            if (chunk !is ProtoKryptonChunk && chunk !is KryptonChunk) return@thenCompose CompletableFuture.completedFuture(ChunkHolder.UNLOADED_CHUNK)
            bringToStatus(x, z, position, ChunkStatus.FULL)
        }
    }

    fun isLoaded(x: Int, z: Int): Boolean = chunkManager.getChunkIfPresent(ChunkPosition.toLong(x, z))?.fullChunk != null

    fun getCachedNow(x: Int, z: Int): KryptonChunk? {
        val k = ChunkPosition.toLong(x, z)
        return getChunkIfPresent(k)?.fullChunk
    }

    fun getLoadedNow(x: Int, z: Int) = getLoadedChunkNow(x, z)

    private fun getNow(x: Int, z: Int) = chunkManager.getChunkIfPresent(ChunkPosition.toLong(x, z))?.lastAvailable

    private fun getLoadedChunkNow(x: Int, z: Int): KryptonChunk? {
        val cacheKey = x and 3 or ((z and 3) shl 2)
        var cached = lastLoadedChunks[cacheKey]
        if (cached != null && cached.x == x && cached.z == z) return lastLoadedChunks[cacheKey]!!
        val chunkKey = ChunkPosition.toLong(x, z)
        cached = loadedChunkMap[chunkKey]
        lastLoadedChunks[cacheKey] = cached
        return cached
    }

    fun getLoadedChunkNoCache(x: Int, z: Int): KryptonChunk? = loadedChunkMap[ChunkPosition.toLong(x, z)]

    private fun updateTicketManager(): Boolean {
        if (!ticketManager.update()) return false
        clearCache()
        return true
    }

    fun save() {
        updateTicketManager()
        chunkManager.saveAll(true)
    }

    fun <T> addTicket(type: TicketType<T>, position: ChunkPosition, level: Int, key: T) = ticketManager.addTicket(type, position, level, key)

    fun <T> removeTicket(type: TicketType<T>, position: ChunkPosition, level: Int, key: T) = ticketManager.removeTicket(type, position, level, key)

    fun <T> addRegionTicket(type: TicketType<T>, position: ChunkPosition, radius: Int, key: T) = ticketManager.addRegionTicket(type, position, radius, key)

    fun <T> removeRegionTicket(type: TicketType<T>, position: ChunkPosition, radius: Int, key: T) = ticketManager.removeRegionTicket(type, position, radius, key)

    fun move(player: KryptonPlayer) = chunkManager.queueMove(player)

    fun trackPlayer(player: KryptonPlayer) = chunkManager.trackPlayer(player)

    fun untrackPlayer(player: KryptonPlayer) = chunkManager.untrackPlayer(player)

    override fun close() {
        save()
        chunkManager.close()
    }

    private fun getChunkIfPresent(position: Long) = chunkManager.getChunkIfPresent(position)

    private fun bringToStatus(x: Int, z: Int, position: ChunkPosition, status: ChunkStatus): CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>> {
        val future = getFuture(x, z, status, true)
        val key = asyncLoadCounter++
        val ticketLevel = ChunkManager.MAX_CHUNK_DISTANCE + status.distance
        addTicket(TicketTypes.ASYNC_LOAD, position, ticketLevel, key)
        return future.thenComposeAsync({
            removeTicket(TicketTypes.ASYNC_LOAD, position, ticketLevel, key)
            addTicket(TicketTypes.TEMPORARY, position, ticketLevel, position) // allow unloading
            if (it.right().isPresent) error("Chunk failed to load: ${it.right().get()}!")
            CompletableFuture.completedFuture(it)
        }, executor)
    }

    private fun getFuture(x: Int, z: Int, status: ChunkStatus, wasUnloading: Boolean): CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>> {
        val position = ChunkPosition(x, z)
        val key = position.toLong()
        val level = 33 + status.distance
        var chunk = getChunkIfPresent(key)
        var currentlyUnloading = false
        if (chunk != null) {
            val oldState = chunk.oldTicketLevel.toFullStatus()
            val currentState = chunk.ticketLevel.toFullStatus()
            currentlyUnloading = (oldState.isOrAfter(FullChunkStatus.BORDER) && !currentState.isOrAfter(FullChunkStatus.BORDER))
        }
        if (wasUnloading && !currentlyUnloading) {
            ticketManager.addTicket(TicketTypes.TEMPORARY, position, level, position)
            if (chunk == null || chunk.oldTicketLevel > level) {
                updateTicketManager()
                chunk = getChunkIfPresent(key)
                if (chunk == null || chunk.oldTicketLevel > level) error("No chunk holder available after ticket has been added!")
            }
        }
        return if (chunk == null || chunk.oldTicketLevel > level) ChunkHolder.UNLOADED_CHUNK_FUTURE else chunk.getOrScheduleFuture(status, chunkManager)
    }

    private fun clearCache() {
        lastChunkPos.fill(ChunkPosition.INVALID)
        lastChunkStatus.fill(null)
        lastChunk.fill(null)
    }

    private fun tickChunks() {
        val worldData = world.data
        val time = worldData.time
        val inhabited = time - lastInhabitedUpdate
        val isDebug = world.isDebug
        val spawnMobs = worldData.gameRules[GameRules.DO_MOB_SPAWNING]
        if (!isDebug) {
            // TODO: Random ticking and mob spawning
        }
        chunkManager.tickTracking()
    }

    companion object {

        private val CHUNK_STATUSES = ChunkStatus.STATUS_LIST
    }
}
