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
package org.kryptonmc.krypton.network

import org.kryptonmc.krypton.network.buffer.BinaryBuffer
import org.kryptonmc.krypton.util.ObjectPool
import org.kryptonmc.krypton.util.readVarInt
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.util.zip.DataFormatException
import java.util.zip.Inflater

object PacketReading {

    @JvmStatic
    fun readPackets(readBuffer: BinaryBuffer, compressed: Boolean, payloadConsumer: PayloadConsumer): BinaryBuffer? {
        var remaining: BinaryBuffer? = null
        val pool = ObjectPool.PACKET_POOL.get()

        while (readBuffer.readableBytes() > 0) {
            val beginMark = readBuffer.mark()
            try {
                // Ensure that the buffer contains the full packet (or wait for next socket read)
                val packetLength = readBuffer.readVarInt()
                val readerStart = readBuffer.readerIndex()
                if (!readBuffer.canRead(packetLength)) throw BufferUnderflowException()

                // Read packet
                var content = readBuffer
                var decompressedSize = packetLength
                if (compressed) {
                    val dataLength = readBuffer.readVarInt()
                    val payloadLength = packetLength - (readBuffer.readerIndex() - readerStart)
                    if (payloadLength < 0) throw DataFormatException("Negative payload length $payloadLength!")

                    if (dataLength == 0) {
                        decompressedSize = payloadLength
                    } else {
                        content = BinaryBuffer.wrap(pool)
                        decompressedSize = dataLength
                        val inflater = Inflater()
                        inflater.setInput(readBuffer.asByteBuffer(readBuffer.readerIndex(), payloadLength))
                        inflater.inflate(content.asByteBuffer(0, dataLength))
                        inflater.reset()
                    }
                }

                // Slice packet
                val payload = content.asByteBuffer(content.readerIndex(), decompressedSize)
                val packetId = payload.readVarInt()
                try {
                    payloadConsumer.consume(packetId, payload)
                } catch (_: Exception) {
                    // Don't care
                }

                // Position buffer to read the next packet
                readBuffer.readerIndex(readerStart + packetLength)
            } catch (_: BufferUnderflowException) {
                readBuffer.reset(beginMark)
                remaining = BinaryBuffer.copy(readBuffer)
                break
            }
        }
        ObjectPool.PACKET_POOL.add(pool)
        return remaining
    }

    fun interface PayloadConsumer {

        fun consume(id: Int, payload: ByteBuffer)
    }
}
