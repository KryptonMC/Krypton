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

import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import kotlin.math.abs
import kotlin.math.max

fun ChunkPosition.checkerboardDistance(player: KryptonPlayer, useWatched: Boolean): Int {
    val x = if (useWatched) player.previousCentralX else player.location.blockX shr 4
    val z = if (useWatched) player.previousCentralZ else player.location.blockZ shr 4
    return checkerboardDistance(x, z)
}

fun ChunkPosition.checkerboardDistance(x: Int, z: Int): Int {
    val diffX = this.x - x
    val diffZ = this.z - z
    return max(abs(diffX), abs(diffZ))
}
