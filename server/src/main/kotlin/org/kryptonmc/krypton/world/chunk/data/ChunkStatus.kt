/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.world.chunk.data

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
