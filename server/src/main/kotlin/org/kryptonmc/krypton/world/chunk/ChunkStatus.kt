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
package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.krypton.world.Heightmap
import java.util.EnumSet

class ChunkStatus private constructor(
    val name: String,
    parent: ChunkStatus?,
    val range: Int,
    private val heightmapsAfter: EnumSet<Heightmap.Type>,
    val type: Type,
) {

    private val parent: ChunkStatus = parent ?: this
    val index: Int = if (parent == null) 0 else parent.index + 1

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

        private val EMPTY = ChunkStatus("empty", null, -1, PRE_FEATURES, Type.PROTO)
        private val STRUCTURE_STARTS = ChunkStatus("structure_starts", EMPTY, 0, PRE_FEATURES, Type.PROTO)
        private val STRUCTURE_REFERENCES = ChunkStatus("structure_references", STRUCTURE_STARTS, 8, PRE_FEATURES, Type.PROTO)
        @JvmField
        val BIOMES: ChunkStatus = ChunkStatus("biomes", STRUCTURE_REFERENCES, 0, PRE_FEATURES, Type.PROTO)
        private val NOISE = ChunkStatus("noise", BIOMES, 8, PRE_FEATURES, Type.PROTO)
        private val SURFACE = ChunkStatus("surface", NOISE, 0, PRE_FEATURES, Type.PROTO)
        private val CARVERS = ChunkStatus("carvers", SURFACE, 0, PRE_FEATURES, Type.PROTO)
        private val LIQUID_CARVERS = ChunkStatus("liquid_carvers", CARVERS, 0, POST_FEATURES, Type.PROTO)
        private val FEATURES = ChunkStatus("features", LIQUID_CARVERS, 8, POST_FEATURES, Type.PROTO)
        private val LIGHT = ChunkStatus("light", FEATURES, 1, POST_FEATURES, Type.PROTO)
        private val SPAWN = ChunkStatus("spawn", LIGHT, 0, POST_FEATURES, Type.PROTO)
        private val HEIGHTMAPS = ChunkStatus("heightmaps", SPAWN, 0, POST_FEATURES, Type.PROTO)
        @JvmField
        val FULL: ChunkStatus = ChunkStatus("full", HEIGHTMAPS, 0, POST_FEATURES, Type.FULL)
    }
}
