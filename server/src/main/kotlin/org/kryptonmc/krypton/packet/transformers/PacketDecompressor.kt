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
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.readVarInt
import java.util.zip.Inflater

/**
 * Decompresses packets that meet or exceed the specified [threshold] in length.
 *
 * @author Callum Seabrook
 */
class PacketDecompressor(var threshold: Int) : ByteToMessageDecoder() {

    private val inflater = Inflater()

    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: MutableList<Any>) {
        if (msg.readableBytes() == 0) return

        val dataLength = msg.readVarInt()
        if (dataLength == 0) {
            out.add(msg.readBytes(msg.readableBytes()))
            return
        }
        if (dataLength < threshold) LOGGER.error("Packet badly compressed! Size of $dataLength is below threshold of $threshold!")
        if (dataLength > PROTOCOL_MAX_SIZE) LOGGER.error("Packet badly compressed! Size of $dataLength is larger than protocol maximum of $PROTOCOL_MAX_SIZE!")

        inflater.setInput(msg.readBytes(msg.readableBytes()).array())
        val bytes = ByteArray(dataLength)
        inflater.inflate(bytes)
        out.add(Unpooled.wrappedBuffer(bytes))
        inflater.reset()
    }

    companion object {

        const val NETTY_NAME = "decompressor"
        private const val PROTOCOL_MAX_SIZE = 0x200000
        private val LOGGER = logger<PacketDecompressor>()
    }
}
