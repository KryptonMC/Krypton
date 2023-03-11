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
            throw DecoderException("Packet badly compressed! Ludicrously large size $dataLength is greater than protocol maximum of " +
                    "$PROTOCOL_MAX_SIZE!")
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

    override fun handlerRemoved0(ctx: ChannelHandlerContext?) {
        compressor.close()
    }

    companion object {

        const val NETTY_NAME: String = "decompressor"
        // Vanilla limit is 2 MB, but we use 16 MB because that's the maximum size we can get away with
        // This isn't just magic! Velocity supports 16 MB as an extended maximum, and so that's what we do as well.
        private const val PROTOCOL_MAX_SIZE = 16 * 1024 * 1024
    }
}
