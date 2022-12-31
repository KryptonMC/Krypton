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
