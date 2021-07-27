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
import it.unimi.dsi.fastutil.ints.IntArrayList
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.generation.Generator
import java.util.EnumSet
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class ChunkStatus private constructor(
    val name: String,
    parent: ChunkStatus?,
    val range: Int,
    val heightmapsAfter: EnumSet<Heightmap.Type>,
    val type: Type,
    val onLoad: LoadTask
) {

    val parent = parent ?: this
    val index: Int = if (parent == null) 0 else parent.index + 1

    val distance: Int
        get() = RANGE_BY_STATUS.getInt(index)

    fun isOrAfter(other: ChunkStatus) = index >= other.index

    fun load(
        world: KryptonWorld,
        chunk: ChunkAccessor,
        function: (ChunkAccessor) -> CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>>
    ) = onLoad(this, world, chunk, function)

    override fun toString() = InternalRegistries.CHUNK_STATUS[this].asString()

    enum class Type {

        PROTO,
        FULL
    }

    fun interface LoadTask {

        operator fun invoke(
            status: ChunkStatus,
            world: KryptonWorld,
            chunk: ChunkAccessor,
            function: (ChunkAccessor) -> CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>>
        ): CompletableFuture<Either<ChunkAccessor, ChunkLoadFailure>>
    }

    companion object {

        private val PRE_FEATURES = EnumSet.of(Heightmap.Type.OCEAN_FLOOR_WG, Heightmap.Type.WORLD_SURFACE_WG)
        private val POST_FEATURES = EnumSet.of(Heightmap.Type.OCEAN_FLOOR, Heightmap.Type.WORLD_SURFACE, Heightmap.Type.MOTION_BLOCKING, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES)
        private val PASSTHROUGH_LOAD_TASK = LoadTask { status, world, chunk, function ->
            if (chunk is ProtoChunk && !chunk.status.isOrAfter(status)) chunk.status = status
            CompletableFuture.completedFuture(Either.left(chunk))
        }

        val EMPTY = register("empty", null, -1, PRE_FEATURES, Type.PROTO)
        val STRUCTURE_STARTS = register("structure_starts", EMPTY, 0, PRE_FEATURES, Type.PROTO)
        val STRUCTURE_REFERENCES = register("structure_references", STRUCTURE_STARTS, 8, PRE_FEATURES, Type.PROTO)
        val BIOMES = register("biomes", STRUCTURE_REFERENCES, 0, PRE_FEATURES, Type.PROTO)
        val NOISE = register("noise", BIOMES, 8, PRE_FEATURES, Type.PROTO)
        val SURFACE = register("surface", NOISE, 0, PRE_FEATURES, Type.PROTO)
        val CARVERS = register("carvers", SURFACE, 0, PRE_FEATURES, Type.PROTO)
        val LIQUID_CARVERS = register("liquid_carvers", CARVERS, 0, POST_FEATURES, Type.PROTO)
        val FEATURES = register("features", LIQUID_CARVERS, 8, POST_FEATURES, Type.PROTO)
        val LIGHT = register("light", FEATURES, 1, POST_FEATURES, Type.PROTO) // TODO: Light chunk on load
        val SPAWN = register("spawn", LIGHT, 0, POST_FEATURES, Type.PROTO)
        val HEIGHTMAPS = register("heightmaps", SPAWN, 0, POST_FEATURES, Type.PROTO)
        val FULL = register("full", HEIGHTMAPS, 0, POST_FEATURES, Type.FULL) { _, _, chunk, function -> function(chunk) }

        val STATUS_LIST = kotlin.run {
            val list = mutableListOf<ChunkStatus>()
            var current = FULL
            while (current.parent !== current) {
                list.add(current)
                current = current.parent
            }
            list.add(current)
            list.reversed()
        }
        val STATUS_BY_RANGE = listOf(FULL, FEATURES, LIQUID_CARVERS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS)
        val RANGE_BY_STATUS = IntArrayList(STATUS_LIST.size).apply {
            var i = 0
            for (j in STATUS_LIST.size - 1 downTo 0) {
                while (i + 1 < STATUS_BY_RANGE.size && j <= STATUS_BY_RANGE[i + 1].index) ++i
                add(0, i)
            }
        }
        val MAX_DISTANCE = STATUS_BY_RANGE.size

        fun fromName(name: String) = InternalRegistries.CHUNK_STATUS[Key.key(name)]

        private fun register(
            name: String,
            parent: ChunkStatus?,
            range: Int,
            heightmapsAfter: EnumSet<Heightmap.Type>,
            type: Type,
            onLoad: LoadTask = PASSTHROUGH_LOAD_TASK
        ): ChunkStatus = Registries.register(InternalRegistries.CHUNK_STATUS, name, ChunkStatus(name, parent, range, heightmapsAfter, type, onLoad))
    }
}

fun Int.statusAroundFull() = if (this >= ChunkStatus.STATUS_BY_RANGE.size) ChunkStatus.EMPTY else if (this < 0) ChunkStatus.FULL else ChunkStatus.STATUS_BY_RANGE[this]
