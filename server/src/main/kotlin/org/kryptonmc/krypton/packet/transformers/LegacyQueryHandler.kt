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
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.krypton.server.StatusConfig
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
class LegacyQueryHandler(private val status: StatusConfig) : ChannelInboundHandlerAdapter() {

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val buf = (msg as ByteBuf).copy()
        buf.markReaderIndex()

        var failed = true
        try {
            if (buf.readUnsignedByte() != 254.toShort()) return

            val address = ctx.channel().remoteAddress() as InetSocketAddress
            val version = ServerInfo.VERSION
            val motd = status.motd.content()
            val playerCount = ServerStorage.PLAYER_COUNT.get()
            val maxPlayers = status.maxPlayers
            when (buf.readableBytes()) {
                0 -> {
                    LOGGER.debug("Legacy server list ping (versions <=1.3.x) received from ${address.address}:${address.port}")
                    ctx.sendFlushAndClose("$motd\u00a7$playerCount\u00a7$maxPlayers".toReply())
                }
                1 -> {
                    if (buf.readUnsignedByte() != 1.toShort()) return
                    LOGGER.debug("Legacy server list ping (versions 1.4.x-1.5.x) received from ${address.address}:${address.port}")
                    ctx.sendFlushAndClose("\u00a71\u0000127\u0000$version\u0000$motd\u0000$playerCount\u0000$maxPlayers".toReply())
                }
                else -> {
                    var isValid = buf.readUnsignedByte() == 1.toShort()
                    isValid = isValid and (buf.readUnsignedByte() == 250.toShort())
                    isValid = isValid and ("MC|PingHost" == String(
                        buf.readAvailableBytes(buf.readShort() * 2),
                        Charsets.UTF_16BE
                    ))
                    val dataLength = buf.readUnsignedShort()
                    isValid = isValid and (buf.readUnsignedByte() >= 73)
                    isValid = isValid and (3 + buf.readAvailableBytes(buf.readShort() * 2).size + 4 == dataLength)
                    isValid = isValid and (buf.readInt() <= 65535)
                    isValid = isValid and (buf.readableBytes() == 0)
                    if (!isValid) return

                    LOGGER.debug("Legacy server list ping (version 1.6.x) received from ${address.address}:${address.port}")
                    ctx.sendFlushAndClose("\u00a71\u0000127\u0000$version\u0000$motd\u0000$playerCount\u0000$maxPlayers".toReply())
                }
            }
            failed = false
        } finally {
            if (failed) {
                buf.resetReaderIndex()
                ctx.channel().pipeline().remove(NETTY_NAME)
                ctx.fireChannelRead(msg)
            }
            buf.release()
        }
    }

    private fun ChannelHandlerContext.sendFlushAndClose(buf: ByteBuf) {
        pipeline().firstContext().writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE)
    }

    private fun String.toReply(): ByteBuf {
        val buf = Unpooled.buffer()
        buf.writeByte(255)
        val chars = toCharArray()
        buf.writeShort(chars.size)
        for (char in chars) buf.writeChar(char.code)
        return buf
    }

    companion object {

        const val NETTY_NAME = "legacy_query"
        private val LOGGER = logger<LegacyQueryHandler>()
    }
}
