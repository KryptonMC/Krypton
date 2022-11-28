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

/**
 * Holds a pair of chunk coordinates (x and z).
 */
// TODO: Look in to removing this class
@JvmRecord
data class ChunkPos(val x: Int, val z: Int) {

    fun pack(): Long = pack(x, z)

    companion object {

        @JvmField
        val ZERO: ChunkPos = ChunkPos(0, 0)

        @JvmStatic
        fun pack(x: Int, z: Int): Long = x.toLong() and 0xFFFFFFFFL or (z.toLong() and 0xFFFFFFFFL shl 32)

        @JvmStatic
        fun unpackX(encoded: Long): Int = (encoded and 0xFFFFFFFFL).toInt()

        @JvmStatic
        fun unpackZ(encoded: Long): Int = (encoded ushr 32 and 0xFFFFFFFFL).toInt()
    }
}
