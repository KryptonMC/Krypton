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
package org.kryptonmc.krypton.world.components

import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.SectionPos
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import org.kryptonmc.krypton.world.chunk.ChunkStatus

interface ChunkGetter : HeightAccessor {

    fun hasChunk(x: Int, z: Int): Boolean

    fun hasChunkAt(x: Int, z: Int): Boolean = hasChunk(SectionPos.blockToSection(x), SectionPos.blockToSection(z))

    fun hasChunkAt(pos: BlockPos): Boolean = hasChunkAt(pos.x, pos.z)

    fun hasChunksAt(from: BlockPos, to: BlockPos): Boolean = hasChunksAt(from.x, from.y, from.z, to.x, to.y, to.z)

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

    fun getChunk(pos: BlockPos): ChunkAccessor? = getChunk(SectionPos.blockToSection(pos.x), SectionPos.blockToSection(pos.z))
}
