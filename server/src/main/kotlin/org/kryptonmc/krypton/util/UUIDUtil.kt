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

import java.util.UUID

object UUIDUtil {

    @JvmField
    val NIL_UUID: UUID = UUID(0L, 0L)

    @JvmStatic
    fun fromIntArray(data: IntArray): UUID = UUID(
        (data[0].toLong() shl 32) or (data[1].toLong() and 0xFFFFFFFFL),
        (data[2].toLong() shl 32) or (data[3].toLong() and 0xFFFFFFFFL)
    )

    @JvmStatic
    fun toIntArray(uuid: UUID): IntArray {
        val most = uuid.mostSignificantBits
        val least = uuid.leastSignificantBits
        return intArrayOf((most shr 32).toInt(), most.toInt(), (least shr 32).toInt(), least.toInt())
    }

    @JvmStatic
    fun createOfflinePlayerId(name: String): UUID = UUID.nameUUIDFromBytes("OfflinePlayer:$name".encodeToByteArray())
}
