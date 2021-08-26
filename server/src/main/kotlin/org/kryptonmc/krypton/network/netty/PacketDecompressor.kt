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
import com.velocitypowered.natives.util.MoreByteBufUtils
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.DecoderException
import org.kryptonmc.krypton.util.readVarInt

/**
 * Decompresses packets that meet or exceed the specified [threshold] in length.
 *
 * Thanks Velocity for the native stuff! :)
 */
class PacketDecompressor(private val compressor: VelocityCompressor, var threshold: Int) : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: MutableList<Any>) {
        if (msg.readableBytes() == 0) return

        val dataLength = msg.readVarInt()
        if (dataLength == 0) {
            out.add(msg.readBytes(msg.readableBytes()))
            return
        }

        // Run insanity checks
        if (dataLength < threshold) {
            throw DecoderException("Packet badly compressed! Size of packet $dataLength is below compression threshold $threshold!")
        }
        if (dataLength > PROTOCOL_MAX_SIZE) {
            throw DecoderException("Packet badly compressed! Ludicrously large size $dataLength is greater than protocol maximum of $PROTOCOL_MAX_SIZE!")
        }

        val compatibleIn = MoreByteBufUtils.ensureCompatible(ctx.alloc(), compressor, msg)
        val uncompressed = MoreByteBufUtils.preferredBuffer(ctx.alloc(), compressor, dataLength)
        try {
            compressor.inflate(compatibleIn, uncompressed, dataLength)
            out.add(uncompressed)
            msg.clear()
        } catch (exception: Exception) {
            uncompressed.release()
            throw exception
        } finally {
            compatibleIn.release()
        }
    }

    override fun handlerRemoved0(ctx: ChannelHandlerContext) = compressor.close()

    companion object {

        const val NETTY_NAME = "decompressor"
        private const val PROTOCOL_MAX_SIZE = 16 * 1024 * 1024 // Vanilla limit is 2 MB, but we use 16 MB because that's the maximum size we can
    }
}
