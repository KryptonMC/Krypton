/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.kryptonmc.krypton.network.SessionHandler
import org.kryptonmc.krypton.packet.PacketRegistry
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.readVarInt

/**
 * Decodes packet data into readable packets and reads the data in by instantiating the packet with the
 * input buffer as the argument.
 */
class PacketDecoder : ByteToMessageDecoder() {

    private var session: SessionHandler? = null // Cache this since the get lookup for classes is a bit expensive

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (buf.readableBytes() == 0) return
        val id = buf.readVarInt()
        val session = session ?: ctx.pipeline().get(SessionHandler::class.java).apply { this@PacketDecoder.session = this }

        val packet = PacketRegistry.lookup(session.currentState, id, buf)
        if (packet == null) {
            LOGGER.debug("Skipping packet with state ${session.currentState} and ID $id because a packet object was not found")
            buf.skipBytes(buf.readableBytes())
            return
        }
        println("Decoded packet ${packet::class.java.simpleName} with ID $id")

        if (buf.readableBytes() != 0) LOGGER.debug("$packet has more bytes available to read.")
        out.add(packet)
    }

    companion object {

        const val NETTY_NAME: String = "decoder"
        private val LOGGER = logger<PacketDecoder>()
    }
}
