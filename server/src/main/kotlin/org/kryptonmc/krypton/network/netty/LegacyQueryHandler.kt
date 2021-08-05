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
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener.CLOSE
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.CorruptedFrameException
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.krypton.config.category.StatusCategory
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.readAvailableBytes
import java.net.InetSocketAddress

/**
 * The legacy query handler is a special handler for special cases. It handles the server list ping packet from
 * old versions of Minecraft.
 *
 * From the words of wiki.vg: "while not technically part of the current protocol, legacy clients may send this to
 * initiate a legacy server list ping, and modern servers should handle it correctly"
 *
 * This handler is essentially a Kotlin converted version of the vanilla handler, which is why it is such a mess.
 */
class LegacyQueryHandler(private val server: KryptonServer) : ChannelInboundHandlerAdapter() {

    private val status = server.config.status

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is ByteBuf) return
        msg.markReaderIndex()

        var failed = true
        try {
            if (msg.readUnsignedByte() != LEGACY_PING_PACKET_ID) return
            val address = ctx.channel().remoteAddress() as InetSocketAddress
            val motd = status.motd.content()
            val maxPlayers = status.maxPlayers
            val playerCount = server.playerManager.players.size
            val version = KryptonPlatform.minecraftVersion

            when (msg.readableBytes()) {
                0 -> {
                    LOGGER.debug("Legacy server list ping (versions <=1.3.x) received from ${address.address}:${address.port}")
                    ctx.writeAndClose("$motd\u00a7$playerCount\u00a7$maxPlayers".toReply())
                }
                1 -> {
                    if (msg.readUnsignedByte() != 1.toShort()) return
                    LOGGER.debug("Legacy server list ping (versions 1.4.x-1.5.x) received from ${address.address}:${address.port}")
                    ctx.writeAndClose("\u00a71\u0000127\u0000$version\u0000$motd\u0000$playerCount\u0000$maxPlayers".toReply())
                }
                else -> {
                    var isValid = msg.readUnsignedByte() == 1.toShort()
                    isValid = isValid and (msg.readUnsignedByte() == 250.toShort())
                    isValid = isValid and (msg.readLegacyString() == MC_1_6_CHANNEL)
                    val dataLength = msg.readUnsignedShort()
                    isValid = isValid and (msg.readUnsignedByte() >= 73)
                    isValid = isValid and (3 + msg.readAvailableBytes(msg.readShort() * 2).size + 4 == dataLength)
                    isValid = isValid and (msg.readInt() <= 65_535)
                    isValid = isValid and (msg.readableBytes() == 0)
                    if (!isValid) return

                    LOGGER.debug("Legacy server list ping (version 1.6.x) received from ${address.address}:${address.port}")
                    val reply = "\u00a71\u0000127\u0000$version\u0000$motd\u0000$playerCount\u0000$maxPlayers".toReply()
                    try {
                        ctx.writeAndClose(reply)
                    } finally {
                        reply.release()
                    }
                }
            }
            msg.release()
            failed = false
        } finally {
            if (failed) {
                msg.resetReaderIndex()
                ctx.channel().pipeline().remove(this)
                ctx.fireChannelRead(msg)
            }
        }
    }

    private fun ChannelHandlerContext.writeAndClose(buf: ByteBuf) = pipeline().firstContext().writeAndFlush(buf).addListener(CLOSE)

    private fun String.toReply() = Unpooled.buffer().writeByte(255).apply {
        val chars = toCharArray()
        writeShort(chars.size)
        chars.forEach { writeChar(it.code) }
    }

    private fun ByteBuf.readLegacyString(): String {
        val length = readShort() * Char.SIZE_BYTES
        if (!isReadable(length)) throw CorruptedFrameException("String length $length is too large for available bytes ${readableBytes()}!")
        val string = toString(readerIndex(), length, Charsets.UTF_16BE)
        skipBytes(length)
        return string
    }

    companion object {

        const val NETTY_NAME = "legacy_query"
        private const val MC_1_6_CHANNEL = "MC|PingHost"
        private const val LEGACY_PING_PACKET_ID: Short = 0xFE
        private val LOGGER = logger<LegacyQueryHandler>()
    }
}
