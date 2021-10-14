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
package org.kryptonmc.krypton

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.kqueue.KQueue
import io.netty.channel.kqueue.KQueueEventLoopGroup
import io.netty.channel.kqueue.KQueueServerSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.ServerSocketChannel
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler
import org.kryptonmc.krypton.network.SessionHandler
import org.kryptonmc.krypton.network.netty.LegacyQueryHandler
import org.kryptonmc.krypton.network.netty.PacketDecoder
import org.kryptonmc.krypton.network.netty.PacketEncoder
import org.kryptonmc.krypton.network.netty.SizeDecoder
import org.kryptonmc.krypton.network.netty.SizeEncoder
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.threadFactory
import java.io.IOException

/**
 * The base Netty connection handler initializer.
 */
object NettyProcess {

    private val LOGGER = logger<KryptonServer>()
    private val bossGroup: EventLoopGroup = bestLoopGroup()
    private val workerGroup: EventLoopGroup = bestLoopGroup()
    private lateinit var future: ChannelFuture

    fun run(server: KryptonServer) {
        try {
            val legacyQueryHandler = LegacyQueryHandler(server)
            val bootstrap = ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(bestChannel())
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline()
                            .addLast("timeout", ReadTimeoutHandler(30))
                            .addLast(LegacyQueryHandler.NETTY_NAME, legacyQueryHandler)
                            .addLast(SizeDecoder.NETTY_NAME, SizeDecoder())
                            .addLast(PacketDecoder.NETTY_NAME, PacketDecoder())
                            .addLast(SizeEncoder.NETTY_NAME, SizeEncoder)
                            .addLast(PacketEncoder.NETTY_NAME, PacketEncoder)
                            .addLast(SessionHandler.NETTY_NAME, SessionHandler(server))
                    }
                })
            future = bootstrap.bind(server.config.server.ip, server.config.server.port)
        } catch (exception: IOException) {
            LOGGER.error("FAILED TO BIND TO PORT ${server.config.server.port}!", exception)
            server.stop()
        }
    }

    fun shutdown() {
        future.channel().close().sync()
    }

    // Determines the best loop group to use based on what is available on the current operating system
    private fun bestLoopGroup() = when {
        Epoll.isAvailable() -> EpollEventLoopGroup(0, threadFactory("Netty Epoll Worker #%d"))
        KQueue.isAvailable() -> KQueueEventLoopGroup(0, threadFactory("Netty KQueue Worker #%d"))
        else -> NioEventLoopGroup(0, threadFactory("Netty NIO Worker #%d"))
    }

    // Determines the best socket channel to use based on what is available on the current operating system
    private fun bestChannel(): Class<out ServerSocketChannel> = when {
        Epoll.isAvailable() -> EpollServerSocketChannel::class.java
        KQueue.isAvailable() -> KQueueServerSocketChannel::class.java
        else -> NioServerSocketChannel::class.java
    }
}
