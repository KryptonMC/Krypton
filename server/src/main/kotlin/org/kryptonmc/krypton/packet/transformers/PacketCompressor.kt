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
package org.kryptonmc.krypton.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.kryptonmc.krypton.util.writeVarInt
import java.util.zip.Deflater

/**
 * Compresses packets that meet or exceed the specified [threshold] in length.
 */
class PacketCompressor(var threshold: Int) : MessageToByteEncoder<ByteBuf>() {

    private val encodeBuffer = ByteArray(8192)
    private val deflater = Deflater()

    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
        val uncompressedSize = msg.readableBytes()
        if (uncompressedSize < threshold) {
            out.writeVarInt(0)
            out.writeBytes(msg)
            return
        }
        val uncompressedBytes = ByteArray(uncompressedSize)
        msg.readBytes(uncompressedBytes)
        out.writeVarInt(uncompressedBytes.size)
        deflater.setInput(uncompressedBytes, 0, uncompressedSize)
        deflater.finish()
        while (!deflater.finished()) {
            val someLength = deflater.deflate(encodeBuffer)
            out.writeBytes(encodeBuffer, 0, someLength)
        }
        deflater.reset()
    }

    companion object {

        const val NETTY_NAME = "compressor"
    }
}
