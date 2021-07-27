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
package org.kryptonmc.krypton.world.chunk

import com.mojang.datafixers.util.Either
import it.unimi.dsi.fastutil.shorts.ShortSet
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.server.chunk.ChunkManager
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.BitSet
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicReferenceArray

class ChunkHolder(
    val position: ChunkPosition,
    ticketLevel: Int,
    private val world: KryptonWorld,
    private val onLevelChange: (ChunkPosition, () -> Int, Int, (Int) -> Unit) -> Unit,
    private val playerProvider: (ChunkPosition, Boolean) -> Sequence<KryptonPlayer>
) {

    private val futures = AtomicReferenceArray<CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>>>(CHUNK_STATUSES.size)
    @Volatile var fullChunkFuture = UNLOADED_FULL_CHUNK_FUTURE
        private set
    @Volatile var isFullChunkReady = false
        private set
    @Volatile var tickingFuture = UNLOADED_FULL_CHUNK_FUTURE
        private set
    @Volatile var entityTickingFuture = UNLOADED_FULL_CHUNK_FUTURE
        private set
    @Volatile var toSave: CompletableFuture<ChunkAccessor?> = CompletableFuture.completedFuture(null)
        private set
    @Volatile private var pendingFullStateConfirmation: CompletableFuture<Unit> = CompletableFuture.completedFuture(Unit)
    @Volatile var oldTicketLevel = ChunkManager.MAX_CHUNK_DISTANCE + 1
    @Volatile var ticketLevel = oldTicketLevel
    @Volatile var queueLevel = oldTicketLevel
    @Volatile private var hasChangedSections = false
    private val changedBlocksBySection = arrayOfNulls<ShortSet>(world.sectionCount)
    @Volatile var wasAccessibleSinceLastSave = false
        private set
    private val blockChangedLightSectionFilter = BitSet()
    private val skyChangedLightSectionFilter = BitSet()
    @Volatile private var resendLight = false
    @Volatile var isUpdateQueued = false

    val tickingChunk: KryptonChunk?
        get() = tickingFuture.getNow(null)?.left()?.orElse(null)
    val lastAvailableStatus: ChunkStatus?
        get() = CHUNK_STATUSES.lastOrNull { getFutureIfPresentUnchecked(it).getNow(UNLOADED_CHUNK).left().isPresent }
    val lastAvailable: ChunkAccessor?
        get() {
            for (i in CHUNK_STATUSES.size - 1 downTo 0) {
                val status = CHUNK_STATUSES[i]
                val future = getFutureIfPresentUnchecked(status)
                if (!future.isCompletedExceptionally) future.getNow(UNLOADED_CHUNK).left().let { if (it.isPresent) return it.get() }
            }
            return null
        }

    val fullChunk: KryptonChunk?
        get() = getFutureIfPresentUnchecked(ChunkStatus.FULL).getNow(null)?.left()?.orElse(null) as? KryptonChunk

    init {
        this.ticketLevel = ticketLevel
    }

    fun getFutureIfPresent(status: ChunkStatus) = if (ticketLevel.toChunkStatus().isOrAfter(status)) getFutureIfPresentUnchecked(status) else UNLOADED_CHUNK_FUTURE

    fun getFutureIfPresentUnchecked(status: ChunkStatus) = futures[status.index] ?: UNLOADED_CHUNK_FUTURE

    fun refreshAccessibility() {
        wasAccessibleSinceLastSave = ticketLevel.toFullStatus().isOrAfter(FullChunkStatus.BORDER)
    }

    fun getOrScheduleFuture(status: ChunkStatus, manager: ChunkManager): CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>> {
        val index = status.index
        val future = futures[index]
        if (future != null) {
            val either = future.getNow(null)
            val present = either != null && either.right().isPresent
            if (!present) return future
        }
        return if (ticketLevel.toChunkStatus().isOrAfter(status)) {
            val scheduled = manager.schedule(this, status)
            updateToSave(scheduled)
            futures.set(index, scheduled)
            scheduled
        } else future ?: UNLOADED_CHUNK_FUTURE
    }

    fun replaceProto(protoKryptonChunk: ProtoKryptonChunk) {
        for (i in 0 until futures.length()) {
            val future = futures[i] ?: continue
            val access = future.getNow(UNLOADED_CHUNK).left()
            if (access.isPresent && access.get() is ProtoChunk) futures.set(i, CompletableFuture.completedFuture(Either.left(protoKryptonChunk)))
        }
        updateToSave(CompletableFuture.completedFuture(Either.left(protoKryptonChunk.wrapped)))
    }

    fun updateFutures(manager: ChunkManager, executor: Executor) {
        val oldStatus = oldTicketLevel.toChunkStatus()
        val currentStatus = ticketLevel.toChunkStatus()
        val oldInRange = oldTicketLevel <= ChunkManager.MAX_CHUNK_DISTANCE
        val currentInRange = ticketLevel <= ChunkManager.MAX_CHUNK_DISTANCE
        val oldFullStatus = oldTicketLevel.toFullStatus()
        val currentFullStatus = ticketLevel.toFullStatus()
        if (oldInRange) {
            val unloaded = Either.right<ChunkAccessor, ChunkLoadFailure>(ChunkLoadFailure("Unloaded ticket level $position"))
            for (i in (if (currentInRange) currentStatus.index + 1 else 0)..oldStatus.index) {
                val future = futures[i]
                if (future == null) futures.set(i, CompletableFuture.completedFuture(unloaded))
            }
        }

        val oldBorder = oldFullStatus.isOrAfter(FullChunkStatus.BORDER)
        val currentBorder = currentFullStatus.isOrAfter(FullChunkStatus.BORDER)
        wasAccessibleSinceLastSave = wasAccessibleSinceLastSave or currentBorder
        if (!oldBorder && currentBorder) {
            fullChunkFuture = manager.prepareAccessible(this)
            schedulePromotion(manager, fullChunkFuture, executor, FullChunkStatus.BORDER)
            updateToSave(fullChunkFuture)
        }
        if (oldBorder && !currentBorder) {
            val oldFuture = fullChunkFuture
            fullChunkFuture = UNLOADED_FULL_CHUNK_FUTURE
            updateToSave(oldFuture.thenApply { either -> either /* TODO: Pack ticks */ })
        }

        val oldTicking = oldFullStatus.isOrAfter(FullChunkStatus.TICKING)
        val currentTicking = currentFullStatus.isOrAfter(FullChunkStatus.TICKING)
        if (!oldTicking && currentTicking) {
            tickingFuture = manager.prepareTicking(this)
            schedulePromotion(manager, tickingFuture, executor, FullChunkStatus.TICKING)
            updateToSave(tickingFuture)
        }
        if (oldTicking && !currentTicking) {
            tickingFuture.complete(UNLOADED_FULL_CHUNK)
            tickingFuture = UNLOADED_FULL_CHUNK_FUTURE
        }

        val oldEntityTicking = oldFullStatus.isOrAfter(FullChunkStatus.ENTITY_TICKING)
        val currentEntityTicking = currentFullStatus.isOrAfter(FullChunkStatus.ENTITY_TICKING)
        if (!oldEntityTicking && currentEntityTicking) {
            if (entityTickingFuture !== UNLOADED_FULL_CHUNK_FUTURE) error("")
            entityTickingFuture = manager.prepareEntityTicking(position)
            schedulePromotion(manager, entityTickingFuture, executor, FullChunkStatus.ENTITY_TICKING)
            updateToSave(entityTickingFuture)
        }
        if (oldEntityTicking && !currentEntityTicking) {
            entityTickingFuture.complete(UNLOADED_FULL_CHUNK)
            entityTickingFuture = UNLOADED_FULL_CHUNK_FUTURE
        }
        if (!currentFullStatus.isOrAfter(oldFullStatus)) demote(manager, currentFullStatus)
        onLevelChange(position, ::queueLevel, ticketLevel) { queueLevel = it }
        oldTicketLevel = ticketLevel
    }

    private fun updateToSave(future: CompletableFuture<out Either<out ChunkAccessor, ChunkLoadFailure>>) {
        toSave = toSave.thenCombine(future) { accessor, either -> either.map({ it }, { accessor }) }
    }

    private fun schedulePromotion(manager: ChunkManager, future: CompletableFuture<Either<KryptonChunk, ChunkLoadFailure>>, executor: Executor, status: FullChunkStatus) {
        pendingFullStateConfirmation.cancel(false)
        val newFuture = CompletableFuture<Unit>()
        newFuture.thenRunAsync({ manager.onFullChunkStatusChange(position, status) }, executor)
        pendingFullStateConfirmation = newFuture
        future.thenAccept { either -> either.ifLeft { newFuture.complete(Unit) } }
    }

    private fun demote(manager: ChunkManager, status: FullChunkStatus) {
        pendingFullStateConfirmation.cancel(false)
        manager.onFullChunkStatusChange(position, status)
    }

    companion object {

        val UNLOADED_CHUNK: Either<ChunkAccessor, ChunkLoadFailure> = Either.right(ChunkLoadFailure.UNLOADED)
        val UNLOADED_CHUNK_FUTURE: CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>> = CompletableFuture.completedFuture(UNLOADED_CHUNK)
        val UNLOADED_FULL_CHUNK: Either<KryptonChunk, ChunkLoadFailure> = Either.right(ChunkLoadFailure.UNLOADED)
        private val UNLOADED_FULL_CHUNK_FUTURE: CompletableFuture<Either<KryptonChunk, ChunkLoadFailure>> = CompletableFuture.completedFuture(UNLOADED_FULL_CHUNK)
        private val CHUNK_STATUSES = ChunkStatus.STATUS_LIST
        private const val BLOCKS_BEFORE_RESEND_FUDGE = 64
    }
}

fun Int.toChunkStatus() = if (this < 33) ChunkStatus.FULL else (this - 33).statusAroundFull()

fun Int.toFullStatus() = FullChunkStatus.values()[(33 - this + 1).clamp(0, FullChunkStatus.values().size - 1)]

enum class FullChunkStatus {

    INACCESSIBLE,
    BORDER,
    TICKING,
    ENTITY_TICKING;

    fun isOrAfter(other: FullChunkStatus) = ordinal >= other.ordinal
}

class ChunkLoadFailure(private val reason: String) {

    override fun toString() = reason

    companion object {

        val UNLOADED = ChunkLoadFailure("UNLOADED")
    }
}
