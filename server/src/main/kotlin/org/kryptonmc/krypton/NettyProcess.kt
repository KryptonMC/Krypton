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
package org.kryptonmc.krypton

import com.velocitypowered.natives.util.Natives
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.ServerChannel
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerDomainSocketChannel
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.kqueue.KQueue
import io.netty.channel.kqueue.KQueueEventLoopGroup
import io.netty.channel.kqueue.KQueueServerDomainSocketChannel
import io.netty.channel.kqueue.KQueueServerSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.unix.DomainSocketAddress
import io.netty.handler.timeout.ReadTimeoutHandler
import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.network.NettyConnection
import org.kryptonmc.krypton.network.handlers.HandshakeHandler
import org.kryptonmc.krypton.network.netty.ChannelInitializeListener
import org.kryptonmc.krypton.network.netty.GroupedPacketHandler
import org.kryptonmc.krypton.network.netty.LegacyQueryHandler
import org.kryptonmc.krypton.network.netty.PacketDecoder
import org.kryptonmc.krypton.network.netty.PacketEncoder
import org.kryptonmc.krypton.network.netty.SizeDecoder
import org.kryptonmc.krypton.network.netty.SizeEncoder
import org.kryptonmc.krypton.util.executor.daemonThreadFactory
import java.net.SocketAddress
import java.util.concurrent.ConcurrentHashMap

/**
 * The handler that starts up and shuts down the Netty connection handler.
 */
object NettyProcess {

    private val LOGGER = LogManager.getLogger(KryptonServer::class.java)
    private val bossGroup = bestLoopGroup()
    private val workerGroup = bestLoopGroup()
    private val listeners = ConcurrentHashMap<Key, ChannelInitializeListener>()
    private var future: ChannelFuture? = null

    @JvmStatic
    fun run(server: KryptonServer, bindAddress: SocketAddress) {
        LOGGER.info("Using ${Natives.compress.loadedVariant} compression from Velocity.")
        LOGGER.info("Using ${Natives.cipher.loadedVariant} cipher from Velocity.")

        val legacyQueryHandler = LegacyQueryHandler(server)
        val bootstrap = ServerBootstrap()
            .group(bossGroup, workerGroup)
            .localAddress(bindAddress)
            .channel(bestChannel(bindAddress is DomainSocketAddress))
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childHandler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(channel: SocketChannel) {
                    val connection = NettyConnection(server)
                    channel.pipeline()
                        .addLast("timeout", ReadTimeoutHandler(30))
                        .addLast(LegacyQueryHandler.NETTY_NAME, legacyQueryHandler)
                        .addLast(GroupedPacketHandler.NETTY_NAME, GroupedPacketHandler)
                        .addLast(SizeDecoder.NETTY_NAME, SizeDecoder())
                        .addLast(PacketDecoder.NETTY_NAME, PacketDecoder())
                        .addLast(SizeEncoder.NETTY_NAME, SizeEncoder)
                        .addLast(PacketEncoder.NETTY_NAME, PacketEncoder)
                        .addLast(NettyConnection.NETTY_NAME, connection)
                    connection.setHandler(HandshakeHandler(server, connection))
                    server.sessionManager.register(connection)
                    if (listeners.isEmpty()) return
                    listeners.values.forEach { it.onInitialize(channel) }
                }
            })
        future = bootstrap.bind().syncUninterruptibly()
    }

    @JvmStatic
    fun shutdown() {
        val future = future ?: return
        future.channel().close().sync()
    }

    /**
     * Adds a listener that will be called after a new channel has been
     * initialised, allowing for extra processing on each new channel, such as
     * adding, replacing, and/or removing handlers in the pipeline, mutating
     * metadata, along with may other purposes.
     *
     * This is designed mostly for use by plugins, however this should NOT be
     * considered public API, as this could change at any time.
     *
     * @param key the key to store the listener by, for removal
     * @param listener the listener to add
     */
    @JvmStatic
    @Suppress("unused") // Unofficial internal API
    fun addListener(key: Key, listener: ChannelInitializeListener) {
        listeners.put(key, listener)
    }

    /**
     * Removes the channel initialise listener with the given [key], if there
     * is one registered, or else does nothing.
     *
     * This is designed mostly for use by plugins, however this should NOT be
     * considered public API, as this could change at any time.
     *
     * @param key the key for the channel initialise listener
     */
    @JvmStatic
    @Suppress("unused") // Unofficial internal API
    fun removeListener(key: Key) {
        listeners.remove(key)
    }

    // Determines the best loop group to use based on what is available on the current operating system
    @JvmStatic
    private fun bestLoopGroup(): EventLoopGroup = when {
        Epoll.isAvailable() -> EpollEventLoopGroup(0, daemonThreadFactory("Netty Epoll Worker #%d"))
        KQueue.isAvailable() -> KQueueEventLoopGroup(0, daemonThreadFactory("Netty KQueue Worker #%d"))
        else -> NioEventLoopGroup(0, daemonThreadFactory("Netty NIO Worker #%d"))
    }

    // Determines the best socket channel to use based on what is available on the current operating system
    @JvmStatic
    private fun bestChannel(domainSocket: Boolean): Class<out ServerChannel> = when {
        Epoll.isAvailable() -> if (domainSocket) EpollServerDomainSocketChannel::class.java else EpollServerSocketChannel::class.java
        KQueue.isAvailable() -> if (domainSocket) KQueueServerDomainSocketChannel::class.java else KQueueServerSocketChannel::class.java
        else -> NioServerSocketChannel::class.java
    }
}
