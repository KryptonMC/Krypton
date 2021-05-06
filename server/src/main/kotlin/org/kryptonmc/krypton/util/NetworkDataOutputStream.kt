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

import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

/**
 * An output stream that wraps a [ByteArrayOutputStream] and allows us to write things like null-separated
 * string lists with ease.
 *
 * Used in the GS4 query handler.
 */
class NetworkDataOutputStream(size: Int) {

    private val outputStream = ByteArrayOutputStream(size)
    private val dataOutputStream = DataOutputStream(outputStream)

    fun write(bytes: ByteArray) = dataOutputStream.write(bytes, 0, bytes.size)

    fun write(string: String) {
        dataOutputStream.writeBytes(string)
        dataOutputStream.write(0)
    }

    fun write(value: Int) = dataOutputStream.write(value)

    fun write(value: Short) = dataOutputStream.writeShort(java.lang.Short.reverseBytes(value).toInt())

    fun reset() = outputStream.reset()

    val byteArray: ByteArray get() = outputStream.toByteArray()
}
