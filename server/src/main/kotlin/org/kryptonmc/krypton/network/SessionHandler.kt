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
package org.kryptonmc.krypton.network

import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.TimeoutException
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.network.handlers.HandshakeHandler
import org.kryptonmc.krypton.network.handlers.PacketHandler
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketState
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect
import org.kryptonmc.krypton.packet.out.play.PacketOutDisconnect
import org.kryptonmc.krypton.util.logger
import java.net.InetSocketAddress

/**
 * A combination of the old channel handler and session classes. This handles
 * both in one class, and offers minor improvements over the previous two, such
 * as the absence of `lateinit` values.
 */
class SessionHandler(private val server: KryptonServer) : SimpleChannelInboundHandler<Packet>() {

    private var _channel: Channel? = null
        set(value) {
            check(field == null) { "Cannot re-set channel!" }
            field = value
        }
    val channel: Channel
        get() = _channel ?: error("Cannot call channel before it is initialized!")

    var latency = 0
    @Volatile
    var currentState: PacketState = PacketState.HANDSHAKE
    @Volatile
    var handler: PacketHandler? = null

    fun send(packet: Packet) {
        channel.writeAndFlush(packet)
    }

    fun disconnect(reason: Component) {
        when (currentState) {
            PacketState.PLAY -> send(PacketOutDisconnect(reason))
            PacketState.LOGIN -> send(PacketOutLoginDisconnect(reason))
            else -> Unit
        }
        handler?.onDisconnect()
        if (channel.isOpen) channel.close().awaitUninterruptibly()
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        _channel = ctx.channel()
        handler = HandshakeHandler(server, this)
        ctx.channel().config().isAutoRead = true
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        disconnect(END_OF_STREAM)
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Packet) {
        if (!ctx.channel().isOpen) return
        handler?.handle(msg)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if (!ctx.channel().isOpen) return
        if (cause is TimeoutException) {
            val address = ctx.channel().remoteAddress() as InetSocketAddress
            LOGGER.debug("Connection from ${address.address}:${address.port} timed out!", cause)
            disconnect(TIMEOUT)
            return
        }

        val disconnectReason = Component.translatable("disconnect.genericReason", Component.text("Internal Exception: $cause"))
        LOGGER.debug("Failed to send or received invalid packet!", cause)
        disconnect(disconnectReason)
        ctx.channel().config().isAutoRead = false
    }

    companion object {

        const val NETTY_NAME = "handler"
        private val LOGGER = logger<SessionHandler>()
        private val END_OF_STREAM = Component.translatable("disconnect.endOfStream")
        private val TIMEOUT = Component.translatable("disconnect.timeout")
    }
}
