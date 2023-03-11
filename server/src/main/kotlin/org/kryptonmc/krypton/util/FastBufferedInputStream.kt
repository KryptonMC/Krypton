/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.util

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
