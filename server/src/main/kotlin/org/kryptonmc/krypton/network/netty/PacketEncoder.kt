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
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.packet.PacketRegistry
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Encodes packets into raw packet data by writing the packet's ID as a var int
 * followed by calling the packet's
 * [write][org.kryptonmc.krypton.packet.Packet.write] function.
 */
@ChannelHandler.Sharable
object PacketEncoder : MessageToByteEncoder<Packet>() {

    const val NETTY_NAME: String = "encoder"
    private val LOGGER = LogManager.getLogger()

    override fun encode(ctx: ChannelHandlerContext, msg: Packet, out: ByteBuf) {
        val id = PacketRegistry.getOutboundPacketId(msg.javaClass)
        try {
            out.writeVarInt(id)
            msg.write(out)
        } catch (exception: Exception) {
            LOGGER.error("Exception trying to send packet ${msg.javaClass} with id $id", exception)
        }
    }
}
