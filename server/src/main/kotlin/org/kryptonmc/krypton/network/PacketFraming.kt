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

import com.velocitypowered.natives.compression.VelocityCompressor
import com.velocitypowered.natives.util.Natives
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketRegistry
import org.kryptonmc.krypton.util.write3ByteVarInt
import org.kryptonmc.krypton.util.write3EmptyBytes
import org.kryptonmc.krypton.util.writeVarInt

object PacketFraming {

    private val COMPRESSOR: ThreadLocal<VelocityCompressor> = ThreadLocal.withInitial { Natives.compress.get().create(4) }
    @Volatile
    private var compressionThreshold = 0

    @JvmStatic
    fun frame(packet: Packet): ByteBuf {
        val buffer = Unpooled.directBuffer()
        writeFramedPacket(buffer, packet)
        return buffer
    }

    @JvmStatic
    fun writeFramedPacket(buf: ByteBuf, packet: Packet) {
        val packetLengthIndex = buf.write3EmptyBytes()
        val startIndex = buf.writerIndex()
        if (compressionThreshold > 0) {
            writeCompressed(buf, packet)
        } else {
            writePacket(buf, packet)
        }
        val totalPacketLength = buf.writerIndex() - startIndex
        buf.write3ByteVarInt(packetLengthIndex, totalPacketLength)
    }

    @JvmStatic
    private fun writeCompressed(buf: ByteBuf, packet: Packet) {
        val dataLengthIndex = buf.write3EmptyBytes()
        val contentIndex = buf.writerIndex()
        writePacket(buf, packet)
        val packetSize = buf.writerIndex() - contentIndex

        val uncompressedLength = if (packetSize >= compressionThreshold) packetSize else 0
        buf.write3ByteVarInt(dataLengthIndex, uncompressedLength)
        if (uncompressedLength > 0) {
            val uncompressedCopy = buf.copy(contentIndex, packetSize)
            buf.writerIndex(contentIndex)
            COMPRESSOR.get().deflate(uncompressedCopy, buf)
            uncompressedCopy.release()
        }
    }

    @JvmStatic
    private fun writePacket(buf: ByteBuf, packet: Packet) {
        buf.writeVarInt(PacketRegistry.getOutboundPacketId(packet.javaClass))
        packet.write(buf)
    }

    @JvmStatic
    fun setCompressionThreshold(threshold: Int) {
        compressionThreshold = threshold
    }
}
