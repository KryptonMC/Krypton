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
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.network.NettyConnection
import org.kryptonmc.krypton.packet.PacketRegistry
import org.kryptonmc.krypton.util.readVarInt
import java.io.IOException

/**
 * Decodes packet data into readable packets and reads the data in by instantiating the packet with the
 * input buffer as the argument.
 */
class PacketDecoder : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (buf.readableBytes() == 0) return
        val id = buf.readVarInt()
        val packetState = ctx.channel().attr(NettyConnection.PACKET_STATE_ATTRIBUTE).get() ?: throw IOException("No packet state set!")

        val packet = PacketRegistry.getInboundPacket(packetState, id, buf)
        if (packet == null) {
            LOGGER.debug("Skipping packet with state $packetState and ID $id because a packet object was not found")
            buf.skipBytes(buf.readableBytes())
            return
        }

        if (buf.readableBytes() != 0) LOGGER.debug("$packet has more bytes available to read after fully reading it.")
        out.add(packet)
    }

    companion object {

        const val NETTY_NAME: String = "decoder"
        private val LOGGER = LogManager.getLogger()
    }
}
