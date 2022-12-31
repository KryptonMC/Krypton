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
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.CorruptedFrameException
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.util.readAvailableBytes
import java.net.InetSocketAddress

/**
 * The legacy query handler is a special handler for special cases. It handles
 * the server list ping packet from old versions of Minecraft.
 *
 * From the words of wiki.vg: "while not technically part of the current
 * protocol, legacy clients may send this to initiate a legacy server list
 * ping, and modern servers should handle it correctly".
 *
 * This handler is essentially a Kotlin converted version of the vanilla
 * handler, which is why it is such a mess.
 */
@ChannelHandler.Sharable
class LegacyServerListPingPacketHandler(private val server: KryptonServer) : ChannelInboundHandlerAdapter() {

    private val status = server.config.status

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is ByteBuf) return
        msg.markReaderIndex()

        var failed = true
        try {
            if (msg.readUnsignedByte() == LEGACY_PING_PACKET_ID) {
                handlePing(ctx, msg)
                msg.release()
                failed = false
                return
            }
        } catch (_: RuntimeException) {
            return
        } finally {
            if (failed) {
                msg.resetReaderIndex()
                ctx.channel().pipeline().remove(this)
                ctx.fireChannelRead(msg)
            }
        }
    }

    private fun handlePing(ctx: ChannelHandlerContext, buf: ByteBuf) {
        val address = ctx.channel().remoteAddress() as InetSocketAddress
        val motd = status.motd.content()
        val maxPlayers = status.maxPlayers
        val playerCount = server.playerManager.players().size
        val version = KryptonPlatform.minecraftVersion

        when (buf.readableBytes()) {
            0 -> {
                LOGGER.debug("Legacy server list ping (versions <=1.3.x) received from ${address.address}:${address.port}")
                sendFlushAndClose(ctx, encodeResponse("$motd§$playerCount§$maxPlayers"))
            }
            1 -> {
                if (buf.readUnsignedByte() != 1.toShort()) return
                LOGGER.debug("Legacy server list ping (versions 1.4.x-1.5.x) received from ${address.address}:${address.port}")
                sendFlushAndClose(ctx, encodeResponse("§1\u0000127\u0000$version\u0000$motd\u0000$playerCount\u0000$maxPlayers"))
            }
            else -> {
                var isValid = buf.readUnsignedByte() == PAYLOAD_1_6
                isValid = isValid and (buf.readUnsignedByte() == PLUGIN_MESSAGE_ID_1_6)
                isValid = isValid and (readLegacyString(buf) == MC_1_6_CHANNEL)
                val dataLength = buf.readUnsignedShort()
                isValid = isValid and (buf.readUnsignedByte() >= MINIMUM_SUPPORTED_1_6_PROTOCOL)
                isValid = isValid and (3 + buf.readAvailableBytes(buf.readShort() * 2).size + 4 == dataLength)
                isValid = isValid and (buf.readInt() <= MAXIMUM_HOSTNAME_LENGTH)
                isValid = isValid and (buf.readableBytes() == 0)
                if (!isValid) return

                LOGGER.debug("Legacy server list ping (version 1.6.x) received from ${address.address}:${address.port}")
                val response = encodeResponse("§1\u0000127\u0000$version\u0000$motd\u0000$playerCount\u0000$maxPlayers")
                try {
                    sendFlushAndClose(ctx, response)
                } finally {
                    response.release()
                }
            }
        }
    }

    companion object {

        const val NETTY_NAME: String = "legacy_query"
        private const val MC_1_6_CHANNEL = "MC|PingHost"
        private const val LEGACY_PING_PACKET_ID: Short = 0xFE
        private val LOGGER = LogManager.getLogger()

        private const val PAYLOAD_1_6: Short = 1
        private const val PLUGIN_MESSAGE_ID_1_6: Short = 0xFA
        private const val MINIMUM_SUPPORTED_1_6_PROTOCOL = 73
        private const val MAXIMUM_HOSTNAME_LENGTH = 65535

        @JvmStatic
        private fun readLegacyString(buf: ByteBuf): String {
            val length = buf.readShort() * Char.SIZE_BYTES
            if (!buf.isReadable(length)) {
                throw CorruptedFrameException("String length $length is too large for available bytes ${buf.readableBytes()}!")
            }
            val string = buf.toString(buf.readerIndex(), length, Charsets.UTF_16BE)
            buf.skipBytes(length)
            return string
        }

        @JvmStatic
        private fun encodeResponse(response: String): ByteBuf {
            val buf = Unpooled.buffer().writeByte(255)
            val chars = response.toCharArray()
            buf.writeShort(chars.size)
            for (char in chars) {
                buf.writeChar(char.code)
            }
            return buf
        }

        @JvmStatic
        private fun sendFlushAndClose(ctx: ChannelHandlerContext, response: ByteBuf) {
            ctx.pipeline().firstContext().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
        }
    }
}
