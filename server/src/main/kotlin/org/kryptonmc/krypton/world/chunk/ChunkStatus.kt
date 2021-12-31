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

import org.kryptonmc.krypton.world.Heightmap
import java.util.EnumSet

class ChunkStatus private constructor(
    val name: String,
    parent: ChunkStatus?,
    val range: Int,
    val heightmapsAfter: EnumSet<Heightmap.Type>,
    val type: Type,
) {

    val parent: ChunkStatus = parent ?: this
    val index: Int = if (parent == null) 0 else parent.index + 1

    fun isOrAfter(other: ChunkStatus): Boolean = index >= other.index

    override fun toString(): String = "ChunkStatus(name=$name, parent=$parent, range=$range, heightmapsAfter=$heightmapsAfter, type=$type)"

    enum class Type {

        PROTO,
        FULL
    }

    companion object {

        private val PRE_FEATURES = EnumSet.of(Heightmap.Type.OCEAN_FLOOR_WG, Heightmap.Type.WORLD_SURFACE_WG)
        private val POST_FEATURES = EnumSet.of(
            Heightmap.Type.OCEAN_FLOOR,
            Heightmap.Type.WORLD_SURFACE,
            Heightmap.Type.MOTION_BLOCKING,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES
        )

        private val EMPTY = create("empty", null, -1, PRE_FEATURES, Type.PROTO)
        private val STRUCTURE_STARTS = create("structure_starts", EMPTY, 0, PRE_FEATURES, Type.PROTO)
        private val STRUCTURE_REFERENCES = create("structure_references", STRUCTURE_STARTS, 8, PRE_FEATURES, Type.PROTO)
        @JvmField val BIOMES: ChunkStatus = create("biomes", STRUCTURE_REFERENCES, 0, PRE_FEATURES, Type.PROTO)
        private val NOISE = create("noise", BIOMES, 8, PRE_FEATURES, Type.PROTO)
        private val SURFACE = create("surface", NOISE, 0, PRE_FEATURES, Type.PROTO)
        private val CARVERS = create("carvers", SURFACE, 0, PRE_FEATURES, Type.PROTO)
        private val LIQUID_CARVERS = create("liquid_carvers", CARVERS, 0, POST_FEATURES, Type.PROTO)
        private val FEATURES = create("features", LIQUID_CARVERS, 8, POST_FEATURES, Type.PROTO)
        private val LIGHT = create("light", FEATURES, 1, POST_FEATURES, Type.PROTO)
        private val SPAWN = create("spawn", LIGHT, 0, POST_FEATURES, Type.PROTO)
        private val HEIGHTMAPS = create("heightmaps", SPAWN, 0, POST_FEATURES, Type.PROTO)
        @JvmField val FULL: ChunkStatus = create("full", HEIGHTMAPS, 0, POST_FEATURES, Type.FULL)

        @JvmStatic
        private fun create(
            name: String,
            parent: ChunkStatus?,
            range: Int,
            heightmapsAfter: EnumSet<Heightmap.Type>,
            type: Type
        ): ChunkStatus = ChunkStatus(name, parent, range, heightmapsAfter, type)
    }
}
