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

import org.jglrxavpok.hephaistos.nbt.NBTIntArray
import java.util.UUID

/**
 * Convert an ordinary string to a protocol string, with its length prefixed.
 */
@Suppress("SpreadOperator")
fun String.toProtocol(): ByteArray {
    val bytes = encodeToByteArray()
    return byteArrayOf(bytes.size.toByte(), *bytes)
}

fun UUID.serialize() = NBTIntArray(intArrayOf(
    (mostSignificantBits shr 32).toInt(),
    (mostSignificantBits and Int.MAX_VALUE.toLong()).toInt(),
    (leastSignificantBits shr 32).toInt(),
    (leastSignificantBits and Int.MAX_VALUE.toLong()).toInt()
))
