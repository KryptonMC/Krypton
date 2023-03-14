/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.network

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
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.network.handlers.HandshakePacketHandler
import org.kryptonmc.krypton.network.netty.ChannelInitializeListener
import org.kryptonmc.krypton.network.netty.GroupedPacketHandler
import org.kryptonmc.krypton.network.netty.LegacyServerListPingPacketHandler
import org.kryptonmc.krypton.network.netty.PacketDecoder
import org.kryptonmc.krypton.network.netty.PacketEncoder
import org.kryptonmc.krypton.network.netty.PacketSizeDecoder
import org.kryptonmc.krypton.network.netty.PacketSizeEncoder
import org.kryptonmc.krypton.util.executor.daemonThreadFactory
import java.net.SocketAddress
import java.util.concurrent.ConcurrentHashMap

/**
 * The handler that starts up and shuts down the Netty connection handler.
 */
object ConnectionInitializer {

    private val LOGGER = LogManager.getLogger(KryptonServer::class.java)
    private val bossGroup = bestLoopGroup()
    private val workerGroup = bestLoopGroup()
    private val listeners = ConcurrentHashMap<Key, ChannelInitializeListener>()
    private var future: ChannelFuture? = null

    @JvmStatic
    fun run(server: KryptonServer, bindAddress: SocketAddress) {
        LOGGER.info("Using ${Natives.compress.loadedVariant} compression from Velocity.")
        LOGGER.info("Using ${Natives.cipher.loadedVariant} cipher from Velocity.")

        val legacyQueryHandler = LegacyServerListPingPacketHandler(server)
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
                        .addLast(LegacyServerListPingPacketHandler.NETTY_NAME, legacyQueryHandler)
                        .addLast(GroupedPacketHandler.NETTY_NAME, GroupedPacketHandler)
                        .addLast(PacketSizeDecoder.NETTY_NAME, PacketSizeDecoder())
                        .addLast(PacketDecoder.NETTY_NAME, PacketDecoder())
                        .addLast(PacketSizeEncoder.NETTY_NAME, PacketSizeEncoder)
                        .addLast(PacketEncoder.NETTY_NAME, PacketEncoder)
                        .addLast(NettyConnection.NETTY_NAME, connection)
                    connection.setHandler(HandshakePacketHandler(server, connection))
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
