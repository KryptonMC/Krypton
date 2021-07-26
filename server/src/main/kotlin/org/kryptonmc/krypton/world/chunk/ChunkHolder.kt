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
import it.unimi.dsi.fastutil.shorts.ShortArraySet
import it.unimi.dsi.fastutil.shorts.ShortSet
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.BitSet
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicReferenceArray

class ChunkHolder(
    private val position: ChunkPosition,
    ticketLevel: Int,
    private val world: KryptonWorld,
    private val onLevelChange: (ChunkPosition, () -> Int, Int, (Int) -> Unit) -> Unit,
    private val playerProvider: (ChunkPosition, Boolean) -> Sequence<KryptonPlayer>
) {

    private val futures = AtomicReferenceArray<CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>>>(CHUNK_STATUSES.size)
    @Volatile var fullChunkFuture = UNLOADED_FULL_CHUNK_FUTURE
        private set
    @Volatile var tickingFuture = UNLOADED_FULL_CHUNK_FUTURE
        private set
    @Volatile var entityTickingFuture = UNLOADED_FULL_CHUNK_FUTURE
        private set
    var toSave: CompletableFuture<ChunkAccessor?> = CompletableFuture.completedFuture(null)
        private set
    private var oldTicketLevel = ChunkManager.MAX_DISTANCE + 1
    var ticketLevel = oldTicketLevel
    var queueLevel = oldTicketLevel
    private var hasChangedSections = false
    private val changedBlocksBySection = arrayOfNulls<ShortSet>(world.sectionCount)
    var wasAccessibleSinceLastSave = false
        private set
    private val blockChangedLightSectionFilter = BitSet()
    private val skyChangedLightSectionFilter = BitSet()
    private var resendLight = false

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

    init {
        this.ticketLevel = ticketLevel
    }

    fun getFutureIfPresent(status: ChunkStatus) = if (ticketLevel.toChunkStatus().isOrAfter(status)) getFutureIfPresentUnchecked(status) else UNLOADED_CHUNK_FUTURE

    fun getFutureIfPresentUnchecked(status: ChunkStatus) = futures[status.index] ?: UNLOADED_CHUNK_FUTURE

    fun refreshAccessibility() {
        wasAccessibleSinceLastSave = ticketLevel.toFullStatus().isOrAfter(FullChunkStatus.BORDER)
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
