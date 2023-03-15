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
package org.kryptonmc.krypton.network.buffer

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel

class BinaryBuffer private constructor(private var nioBuffer: ByteBuffer) {

    private val capacity = nioBuffer.capacity()
    private var readerIndex = 0
    private var writerIndex = 0

    fun write(buffer: ByteBuffer, index: Int, length: Int) {
        nioBuffer.put(writerIndex, buffer, index, length)
        writerIndex += length
    }

    fun write(buffer: ByteBuffer) {
        write(buffer, buffer.position(), buffer.remaining())
    }

    fun write(buffer: BinaryBuffer) {
        write(asByteBuffer(buffer.readerIndex, buffer.writerIndex - buffer.readerIndex))
    }

    fun readVarInt(): Int {
        var result = 0
        for (i in 0 until 5) {
            val index = readerIndex + i
            val next = nioBuffer.get(index).toInt()
            result = result or ((next and 0x7F) shl (i * 7))
            if (next and 0x80 != 128) {
                readerIndex = index + 1
                return result
            }
        }
        throw RuntimeException("VarInt too big!")
    }

    fun mark(): Marker = Marker(readerIndex, writerIndex)

    fun reset(reader: Int, writer: Int) {
        readerIndex = reader
        writerIndex = writer
    }

    fun reset(marker: Marker) {
        reset(marker.readerIndex, marker.writerIndex)
    }

    fun canRead(size: Int): Boolean = readerIndex + size <= writerIndex

    fun canWrite(size: Int): Boolean = writerIndex + size < capacity

    fun capacity(): Int = capacity

    fun readerIndex(): Int = readerIndex

    fun readerIndex(index: Int) {
        readerIndex = index
    }

    fun writerIndex(): Int = writerIndex

    fun writerIndex(index: Int) {
        writerIndex = index
    }

    fun readableBytes(): Int = writerIndex - readerIndex

    fun readBytes(length: Int): ByteArray {
        val bytes = ByteArray(length)
        nioBuffer.get(readerIndex, bytes)
        readerIndex += length
        return bytes
    }

    fun clear(): BinaryBuffer = apply {
        readerIndex = 0
        writerIndex = 0
        nioBuffer.limit(capacity)
    }

    fun asByteBuffer(reader: Int, length: Int): ByteBuffer = nioBuffer.slice(reader, length)

    fun writeChannel(channel: WritableByteChannel): Boolean {
        if (readerIndex == writerIndex) return true // Nothing to write
        val writeBuffer = nioBuffer.slice(readerIndex, writerIndex - readerIndex)
        val count = channel.write(writeBuffer)
        if (count == -1) {
            // EOS
            throw IOException("Disconnected")
        }
        readerIndex += count
        return writeBuffer.limit() == writeBuffer.position()
    }

    fun readChannel(channel: ReadableByteChannel) {
        val count = channel.read(nioBuffer.slice(writerIndex, capacity - writerIndex))
        if (count == -1) {
            // EOS
            throw IOException("Disconnected")
        }
        writerIndex += count
    }

    @JvmRecord
    data class Marker(val readerIndex: Int, val writerIndex: Int)

    companion object {

        @JvmStatic
        fun sized(size: Int): BinaryBuffer = BinaryBuffer(ByteBuffer.allocateDirect(size))

        @JvmStatic
        fun wrap(buffer: ByteBuffer): BinaryBuffer = BinaryBuffer(buffer)

        @JvmStatic
        fun copy(buffer: BinaryBuffer): BinaryBuffer {
            val size = buffer.readableBytes()
            val temp = ByteBuffer.allocateDirect(size).put(buffer.asByteBuffer(0, size))
            val newBuffer = BinaryBuffer(temp)
            newBuffer.writerIndex = size
            return newBuffer
        }
    }
}
