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

    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        compressor.close()
    }

    companion object {

        const val NETTY_NAME: String = "compressor"
    }
}
