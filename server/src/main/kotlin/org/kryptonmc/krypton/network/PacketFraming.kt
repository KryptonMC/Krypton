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

import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.FramedPacket
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketRegistry
import org.kryptonmc.krypton.util.ObjectPool
import org.kryptonmc.krypton.util.writeEmptyVarIntHeader
import org.kryptonmc.krypton.util.writeVarIntHeader
import java.nio.ByteBuffer
import java.util.zip.Deflater

object PacketFraming {

    private val COMPRESSOR: ThreadLocal<Deflater> = ThreadLocal.withInitial { Deflater() }
    @Volatile
    private var compressionThreshold = 0

    @JvmStatic
    fun frame(packet: Packet): FramedPacket {
        ObjectPool.PACKET_POOL.hold().use { holder ->
            val temp = writeFramedPacket(holder.get(), packet)
            val size = temp.remaining()
            val buffer = ByteBuffer.allocateDirect(size).put(0, temp, 0, size)
            return FramedPacket(packet, buffer)
        }
    }

    @JvmStatic
    fun writeFramedPacket(buffer: ByteBuffer, packet: Packet): ByteBuffer = writeFramedPacket(buffer, packet, compressionThreshold > 0)

    @JvmStatic
    fun writeFramedPacket(buffer: ByteBuffer, packet: Packet, compressed: Boolean): ByteBuffer {
        val threshold = if (compressed) compressionThreshold else 0
        writeFramedPacket(buffer, PacketRegistry.getOutboundPacketId(packet.javaClass), packet, threshold)
        return buffer.flip()
    }

    @JvmStatic
    private fun writeFramedPacket(buffer: ByteBuffer, id: Int, writable: Writable, compressionThreshold: Int) {
        val writer = BinaryWriter(buffer)
        if (compressionThreshold <= 0) {
            val lengthIndex = buffer.writeEmptyVarIntHeader()
            writer.writeVarInt(id)
            writable.write(writer)
            val finalSize = buffer.position() - (lengthIndex + 3)
            buffer.writeVarIntHeader(lengthIndex, finalSize)
            return
        }
        val compressedIndex = buffer.writeEmptyVarIntHeader()
        val uncompressedIndex = buffer.writeEmptyVarIntHeader()

        val contentStart = buffer.position()
        writer.writeVarInt(id)
        writable.write(writer)

        val packetSize = buffer.position() - contentStart
        val compressed = packetSize >= compressionThreshold
        if (compressed) {
            ObjectPool.PACKET_POOL.hold().use { holder ->
                val input = holder.get().put(0, buffer, contentStart, packetSize)
                val compressor = COMPRESSOR.get()
                compressor.setInput(input.limit(packetSize))
                compressor.finish()
                compressor.deflate(buffer.position(contentStart))
                compressor.reset()
            }
        }

        buffer.writeVarIntHeader(compressedIndex, buffer.position() - uncompressedIndex)
        buffer.writeVarIntHeader(uncompressedIndex, if (compressed) packetSize else 0)
    }

    @JvmStatic
    fun setCompressionThreshold(threshold: Int) {
        compressionThreshold = threshold
    }
}
