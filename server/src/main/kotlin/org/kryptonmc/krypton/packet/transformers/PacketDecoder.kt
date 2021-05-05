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
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.kryptonmc.krypton.packet.ChannelHandler
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.readVarInt

/**
 * Decodes packet data into readable packets and reads the data in by calling this packet's
 * [read][org.kryptonmc.krypton.packet.Packet.read] function
 */
class PacketDecoder : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (buf.readableBytes() == 0) return

        val id = buf.readVarInt()
        val session = ctx.pipeline().get(ChannelHandler::class.java).session

        val packet = session.currentState.createPacket(id)
        if (packet == null) {
            LOGGER.debug("Skipping packet with state ${session.currentState} and ID $id because a packet object was not found")
            buf.skipBytes(buf.readableBytes())
            return
        }

        LOGGER.debug("Incoming packet of type ${packet.javaClass}")

        packet.read(buf)

        if (buf.readableBytes() != 0) {
            LOGGER.debug("More bytes from packet $packet (${buf.readableBytes()})")
        }

        out.add(packet)
    }

    companion object {

        const val NETTY_NAME = "decoder"
        private val LOGGER = logger<PacketDecoder>()
    }
}
