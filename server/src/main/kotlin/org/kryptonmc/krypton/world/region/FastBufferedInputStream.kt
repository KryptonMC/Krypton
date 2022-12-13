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
package org.kryptonmc.krypton.world.region

import java.io.InputStream

class FastBufferedInputStream(private val input: InputStream, bufferSize: Int = 8192) : InputStream() {

    private val buffer = ByteArray(bufferSize)
    private var limit = 0
    private var position = 0

    override fun read(): Int {
        if (position >= limit) {
            fill()
            if (position >= limit) return -1
        }
        return java.lang.Byte.toUnsignedInt(buffer[position++])
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        var bufferedBytes = bytesInBuffer()
        if (bufferedBytes <= 0) {
            if (len >= buffer.size) return input.read(b, off, len)
            fill()
            bufferedBytes = bytesInBuffer()
            if (bufferedBytes <= 0) return -1
        }
        var tempLength = len
        if (tempLength > bufferedBytes) tempLength = bufferedBytes
        System.arraycopy(buffer, position, b, off, tempLength)
        position += tempLength
        return tempLength
    }

    override fun skip(n: Long): Long {
        if (n <= 0L) return 0L
        val bufferedBytes = bytesInBuffer()
        if (bufferedBytes <= 0L) return input.skip(n)
        var tempCount = n
        if (tempCount > bufferedBytes) tempCount = bufferedBytes.toLong()
        position = (position + tempCount).toInt()
        return tempCount
    }

    override fun available(): Int = bytesInBuffer() + input.available()

    override fun close() {
        input.close()
    }

    private fun bytesInBuffer(): Int = limit - position

    private fun fill() {
        limit = 0
        position = 0
        val size = input.read(buffer, 0, buffer.size)
        if (size > 0) limit = size
    }
}
