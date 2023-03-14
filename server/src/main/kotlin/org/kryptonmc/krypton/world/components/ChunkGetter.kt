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
package org.kryptonmc.krypton.world.components

import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.coordinate.SectionPos
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import org.kryptonmc.krypton.world.chunk.data.ChunkStatus

interface ChunkGetter : HeightAccessor {

    fun hasChunk(x: Int, z: Int): Boolean

    fun hasChunkAt(x: Int, z: Int): Boolean = hasChunk(SectionPos.blockToSection(x), SectionPos.blockToSection(z))

    fun hasChunkAt(pos: Vec3i): Boolean = hasChunkAt(pos.x, pos.z)

    fun hasChunksAt(from: Vec3i, to: Vec3i): Boolean = hasChunksAt(from.x, from.y, from.z, to.x, to.y, to.z)

    fun hasChunksAt(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Boolean =
        if (y2 >= minimumBuildHeight() && y1 < maximumBuildHeight()) hasChunksAt(x1, z1, x2, z2) else false

    fun hasChunksAt(x1: Int, z1: Int, x2: Int, z2: Int): Boolean {
        val fromX = SectionPos.blockToSection(x1)
        val toX = SectionPos.blockToSection(x2)
        val fromZ = SectionPos.blockToSection(z1)
        val toZ = SectionPos.blockToSection(z2)
        for (x in fromX..toX) {
            for (z in fromZ..toZ) {
                if (!hasChunk(x, z)) return false
            }
        }
        return true
    }

    fun getChunk(x: Int, z: Int, requiredStatus: ChunkStatus, shouldCreate: Boolean): ChunkAccessor?

    fun getChunk(x: Int, z: Int, requiredStatus: ChunkStatus): ChunkAccessor? = getChunk(x, z, requiredStatus, true)

    fun getChunk(x: Int, z: Int): ChunkAccessor? = getChunk(x, z, ChunkStatus.FULL, true)
}
