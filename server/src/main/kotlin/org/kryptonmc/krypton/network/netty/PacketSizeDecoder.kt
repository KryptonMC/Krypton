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

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.CorruptedFrameException
import org.kryptonmc.krypton.util.readVarInt

/**
 * Decodes the size of a packet.
 */
class PacketSizeDecoder : ByteToMessageDecoder() {

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

        const val NETTY_NAME: String = "splitter"
    }
}
