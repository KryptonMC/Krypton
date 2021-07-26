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
package org.kryptonmc.krypton.server.chunk

import org.kryptonmc.krypton.util.DynamicGraphMinFixedPoint
import org.kryptonmc.krypton.world.chunk.ChunkPosition

abstract class ChunkTracker(levelCount: Int, expectedLevelSize: Int, expectedTotalSize: Int) : DynamicGraphMinFixedPoint(levelCount, expectedLevelSize, expectedTotalSize) {

    protected abstract fun getLevelFromSource(id: Long): Int

    fun update(position: Long, distance: Int, decrease: Boolean) = checkEdge(ChunkPosition.INVALID, position, distance, decrease)

    override fun isSource(id: Long) = id == ChunkPosition.INVALID

    override fun checkNeighbours(id: Long, level: Int, decrease: Boolean) {
        val x = id.toInt()
        val z = (id shr 32).toInt()
        for (xo in -1..1) {
            for (zo in -1..1) {
                val pos = ChunkPosition.toLong(x + xo, z + zo)
                if (pos != id) checkNeighbour(id, pos, level, decrease)
            }
        }
    }

    override fun getComputedLevel(id: Long, excludedId: Long, maxLevel: Int): Int {
        var max = maxLevel
        val x = id.toInt()
        val z = (id shr 32).toInt()
        for (xo in -1..1) {
            for (zo in -1..1) {
                var pos = ChunkPosition.toLong(x + xo, z + zo)
                if (pos == id) pos = ChunkPosition.INVALID
                if (pos != excludedId) {
                    val computed = computeLevelFromNeighbour(pos, id, getLevel(pos))
                    if (max > computed) max = computed
                    if (max == 0) return max
                }
            }
        }
        return max
    }

    override fun computeLevelFromNeighbour(sourceId: Long, targetId: Long, level: Int) = if (sourceId == ChunkPosition.INVALID) getLevelFromSource(targetId) else level + 1
}
