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
package org.kryptonmc.krypton.network.netty

import com.velocitypowered.natives.compression.VelocityCompressor
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Compresses packets that meet or exceed the specified [threshold] in length.
 *
 * Thanks Velocity for the fast var ints and native stuff!
 */
class PacketCompressor(private val compressor: VelocityCompressor, var threshold: Int) : MessageToByteEncoder<ByteBuf>() {

    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
        val uncompressedSize = msg.readableBytes()
        if (uncompressedSize < threshold) {
            out.writeVarInt(0)
            out.writeBytes(msg)
            return
        }
        out.writeVarInt(uncompressedSize)
        compressor.deflate(msg, out)
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext?) = compressor.close()

    companion object {

        const val NETTY_NAME = "compressor"
    }
}
