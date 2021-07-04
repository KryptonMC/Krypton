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

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Encodes packets into raw packet data by writing the packet's ID as a varint followed by calling the packet's
 * [write][org.kryptonmc.krypton.packet.Packet.write] function
 */
@ChannelHandler.Sharable
object PacketEncoder : MessageToByteEncoder<Packet>() {

    const val NETTY_NAME = "encoder"
    private val LOGGER = logger<PacketEncoder>()

    override fun encode(ctx: ChannelHandlerContext, msg: Packet, out: ByteBuf) = try {
        LOGGER.debug("Outgoing packet of type ${msg.javaClass} id ${msg.info.id}")
        out.writeVarInt(msg.info.id)
        msg.write(out)
    } catch (exception: Exception) {
        LOGGER.error("Exception trying to send packet ${msg.javaClass} with id ${msg.info.id}", exception)
    }
}
