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
package org.kryptonmc.krypton.util

import org.kryptonmc.krypton.entity.EntityManager
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.spongepowered.math.vector.Vector3d
import kotlin.math.abs

fun Vector3d.isInSpawnableBounds() = !floorX().isOutsideSpawnableHeight() && isInHorizontalWorldBounds()

private fun Int.isOutsideSpawnableHeight() = this < -20000000 || this >= 20000000

private fun Vector3d.isInHorizontalWorldBounds() = floorX() >= -30000000 && floorZ() >= -30000000 &&
        floorX() < 30000000 && floorZ() < 30000000

fun EntityManager.forEachEntityInRange(location: Vector3d, viewDistance: Int, callback: (KryptonEntity) -> Unit) {
    val chunksInRange = location.chunksInRange(viewDistance)
    chunksInRange.forEach {
        val chunk = world.getChunkAt(it.chunkX(), it.chunkZ()) ?: return@forEach
        get(chunk).forEach(callback)
    }
}

private fun Long.chunkX() = (this and 4294967295L).toInt()

private fun Long.chunkZ() = (this ushr 32 and 4294967295L).toInt()

private fun Vector3d.chunksInRange(range: Int): LongArray {
    val visible = LongArray(range.toArea())
    var xDistance = 0
    var xDirection = 1
    var zDistance = 0
    var zDirection = -1
    var length = 1
    var corner = 0

    for (i in visible.indices) {
        val chunkX = (xDistance * 16 + x()).toChunkCoordinate()
        val chunkZ = (zDistance * 16 + z()).toChunkCoordinate()
        visible[i] = ChunkPosition.toLong(chunkX, chunkZ)

        if (corner % 2 == 0) {
            xDistance += xDirection
            if (abs(xDistance) == length) {
                corner++
                xDirection = -xDirection
            }
        } else {
            zDistance += zDirection
            if (abs(zDistance) == length) {
                corner++
                zDirection = -zDirection
                if (corner % 4 == 0) length++
            }
        }
    }
    return visible
}

private fun Double.toChunkCoordinate(): Int = Math.floorDiv(floor(), 16)
