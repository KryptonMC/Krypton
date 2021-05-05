/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.CorruptedFrameException
import org.kryptonmc.krypton.util.readVarInt

/**
 * Decodes the size of a packet.
 */
class SizeDecoder : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        buf.markReaderIndex()

        val buffer = ByteArray(3)
        for (i in buffer.indices) {
            if (!buf.isReadable) {
                buf.resetReaderIndex()
                return
            }
            buffer[i] = buf.readByte()
            if (buffer[i] < 0) continue

            val length = Unpooled.wrappedBuffer(buffer).readVarInt()
            if (buf.readableBytes() < length) {
                buf.resetReaderIndex()
                return
            }
            out.add(buf.readBytes(length))
            return
        }
        throw CorruptedFrameException("length wider than 21 bits")
    }

    companion object {

        const val NETTY_NAME = "splitter"
    }
}
