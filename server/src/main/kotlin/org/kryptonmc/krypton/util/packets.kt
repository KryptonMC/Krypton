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

import com.velocitypowered.natives.compression.VelocityCompressor
import com.velocitypowered.natives.util.Natives
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketRegistry

fun Packet.frame(compressionThreshold: Int): ByteBuf {
    val buffer = Unpooled.directBuffer()
    buffer.writeFramedPacket(this, compressionThreshold)
    return buffer
}

fun ByteBuf.writeFramedPacket(packet: Packet, compressionThreshold: Int) {
    val packetLengthIndex = write3EmptyBytes()
    val startIndex = writerIndex()
    if (compressionThreshold > 0) {
        val dataLengthIndex = write3EmptyBytes()
        val contentIndex = writerIndex()
        writePacket(packet)
        val packetSize = writerIndex() - contentIndex

        val uncompressedLength = if (packetSize >= compressionThreshold) packetSize else 0
        write3ByteVarInt(dataLengthIndex, uncompressedLength)
        if (uncompressedLength > 0) {
            val uncompressedCopy = copy(contentIndex, packetSize)
            writerIndex(contentIndex)
            Grouping.COMPRESSOR.get().deflate(uncompressedCopy, this)
            uncompressedCopy.release()
        }
    } else {
        writePacket(packet)
    }
    val totalPacketLength = writerIndex() - startIndex
    write3ByteVarInt(packetLengthIndex, totalPacketLength)
}

private fun ByteBuf.writePacket(packet: Packet) {
    writeVarInt(PacketRegistry.lookup(packet.javaClass))
    packet.write(this)
}

private object Grouping {

    @JvmField
    val COMPRESSOR: ThreadLocal<VelocityCompressor> = ThreadLocal.withInitial { Natives.compress.get().create(4) }
}
