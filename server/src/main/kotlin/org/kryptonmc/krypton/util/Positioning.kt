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
package org.kryptonmc.krypton.util

import org.kryptonmc.api.util.Vec3d
import kotlin.math.abs

object Positioning {

    /**
     * Calculates the change in position between the given [new] and [old] coordinates.
     * No idea why Mojang thought having player coordinates be absolute and entity
     * coordinates be relative.
     *
     * See [here](https://wiki.vg/Protocol#Entity_Position)
     */
    @JvmStatic
    fun delta(new: Double, old: Double): Short = ((new * 32 - old * 32) * 128).toInt().toShort()

    /**
     * Encodes the given [velocity] in to the protocol's standard velocity
     * units, measured in 1/8000 of a block per server tick.
     */
    @JvmStatic
    fun encodeVelocity(velocity: Double): Int = (Maths.clamp(velocity, -3.9, 3.9) * 8000.0).toInt()

    /**
     * Checks if the change between the old and the new is within the range
     * allowed by the entity move packet.
     *
     * The entity move packet allows at most 8 blocks in any direction, because
     * it calculates the delta as shown in [delta], the range of a short is
     * -32768 to 32767, and 32768 / (128 * 32) = 8.
     *
     * See [here](https://wiki.vg/Protocol#Entity_Position)
     */
    @JvmStatic
    fun deltaInMoveRange(old: Vec3d, new: Vec3d): Boolean = abs(new.x - old.x) > 8 || abs(new.y - old.y) > 8 || abs(new.z - old.z) > 8

    @JvmStatic
    fun toChunkCoordinate(value: Int): Int = value shr 4

    @JvmStatic
    fun chunkX(encoded: Long): Int = encoded.toInt()

    @JvmStatic
    fun chunkZ(encoded: Long): Int = (encoded ushr 32).toInt()

    @JvmStatic
    fun decodeBlockX(encoded: Long): Int = (encoded shr 38).toInt()

    @JvmStatic
    fun decodeBlockY(encoded: Long): Int = (encoded and 0xFFF).toInt()

    @JvmStatic
    fun decodeBlockZ(encoded: Long): Int = (encoded shl 26 shr 38).toInt()
}
