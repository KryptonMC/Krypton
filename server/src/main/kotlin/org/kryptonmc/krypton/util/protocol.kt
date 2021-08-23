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

/**
 * Convert an ordinary string to a protocol string, with its length prefixed.
 *
 * We manually copy the bytes to avoid using the spread operator.
 */
fun String.toProtocol(): ByteArray {
    val bytes = encodeToByteArray()
    val array = ByteArray(bytes.size + 1)
    array[0] = bytes.size.toByte()
    for (i in bytes.indices) {
        array[i + 1] = bytes[i]
    }
    return array
}
