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

import it.unimi.dsi.fastutil.ints.IntArrayList
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.world.Heightmap
import java.util.EnumSet

class ChunkStatus private constructor(
    val name: String,
    parent: ChunkStatus?,
    val range: Int,
    val heightmapsAfter: EnumSet<Heightmap.Type>,
    val type: Type,
) {

    val parent = parent ?: this
    val index: Int = if (parent == null) 0 else parent.index + 1

    val distance by lazy { RANGE_BY_STATUS.getInt(index) }

    fun isOrAfter(other: ChunkStatus) = index >= other.index

    override fun toString() = InternalRegistries.CHUNK_STATUS[this].asString()

    enum class Type {

        PROTO,
        FULL
    }

    companion object {

        private val PRE_FEATURES = EnumSet.of(Heightmap.Type.OCEAN_FLOOR_WG, Heightmap.Type.WORLD_SURFACE_WG)
        private val POST_FEATURES = EnumSet.of(Heightmap.Type.OCEAN_FLOOR, Heightmap.Type.WORLD_SURFACE, Heightmap.Type.MOTION_BLOCKING, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES)

        val EMPTY = register("empty", null, -1, PRE_FEATURES, Type.PROTO)
        val STRUCTURE_STARTS = register("structure_starts", EMPTY, 0, PRE_FEATURES, Type.PROTO)
        val STRUCTURE_REFERENCES = register("structure_references", STRUCTURE_STARTS, 8, PRE_FEATURES, Type.PROTO)
        val BIOMES = register("biomes", STRUCTURE_REFERENCES, 0, PRE_FEATURES, Type.PROTO)
        val NOISE = register("noise", BIOMES, 8, PRE_FEATURES, Type.PROTO)
        val SURFACE = register("surface", NOISE, 0, PRE_FEATURES, Type.PROTO)
        val CARVERS = register("carvers", SURFACE, 0, PRE_FEATURES, Type.PROTO)
        val LIQUID_CARVERS = register("liquid_carvers", CARVERS, 0, POST_FEATURES, Type.PROTO)
        val FEATURES = register("features", LIQUID_CARVERS, 8, POST_FEATURES, Type.PROTO)
        val LIGHT = register("light", FEATURES, 1, POST_FEATURES, Type.PROTO)
        val SPAWN = register("spawn", LIGHT, 0, POST_FEATURES, Type.PROTO)
        val HEIGHTMAPS = register("heightmaps", SPAWN, 0, POST_FEATURES, Type.PROTO)
        val FULL = register("full", HEIGHTMAPS, 0, POST_FEATURES, Type.FULL)

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
        val RANGE_BY_STATUS = IntArrayList()
        val MAX_DISTANCE = STATUS_BY_RANGE.size

        private fun register(name: String, parent: ChunkStatus?, range: Int, heightmapsAfter: EnumSet<Heightmap.Type>, type: Type): ChunkStatus =
            Registries.register(InternalRegistries.CHUNK_STATUS, name, ChunkStatus(name, parent, range, heightmapsAfter, type))
    }
}

fun Int.statusAroundFull() = if (this >= ChunkStatus.STATUS_BY_RANGE.size) ChunkStatus.EMPTY else if (this < 0) ChunkStatus.FULL else ChunkStatus.STATUS_BY_RANGE[this]
