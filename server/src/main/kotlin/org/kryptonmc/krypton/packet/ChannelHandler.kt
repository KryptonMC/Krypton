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
package org.kryptonmc.krypton.packet

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.packet.session.Session
import org.kryptonmc.krypton.util.logger
import java.net.InetSocketAddress
import java.util.concurrent.TimeoutException

/**
 * This is the handler at the end of the Netty pipeline that does the actual processing of
 * inbound packets (after they have been processed and turned into Packet objects)
 */
class ChannelHandler(private val server: KryptonServer) : SimpleChannelInboundHandler<Packet>() {

    /**
     * The session this handler handles
     */
    internal lateinit var session: Session

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        LOGGER.debug("[+] Channel Connected: ${ctx.channel().remoteAddress()}")
        session = Session(server, ctx.channel())
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        LOGGER.debug("[-] Channel Disconnected: ${ctx.channel().remoteAddress()}")
        session.handler.onDisconnect()
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Packet) {
        if (!ctx.channel().isOpen) return
        session.handler.handle(msg)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if (!ctx.channel().isOpen) return

        if (cause is TimeoutException) {
            val address = ctx.channel().remoteAddress() as InetSocketAddress
            LOGGER.debug("Connection from ${address.address}:${address.port} timed out. Reason: ", cause)
            session.disconnect(translatable("disconnect.timeout"))
        } else {
            val disconnectReason = translatable("disconnect.genericReason", listOf(Messages.NETWORK.HANDLER_ERROR_DISCONNECT()))
            LOGGER.debug("Failed to send or received invalid packet! Cause: ", cause)
            session.disconnect(disconnectReason)
            ctx.channel().config().isAutoRead = false
        }
    }

    companion object {

        const val NETTY_NAME = "handler"

        private val LOGGER = logger<ChannelHandler>()
    }
}
