package org.kryptonmc.krypton

import io.netty.bootstrap.ServerBootstrap
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
import io.netty.incubator.channel.uring.IOUring
import io.netty.incubator.channel.uring.IOUringEventLoopGroup
import io.netty.incubator.channel.uring.IOUringServerSocketChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kryptonmc.krypton.packet.ChannelHandler
import org.kryptonmc.krypton.packet.transformers.LegacyQueryHandler
import org.kryptonmc.krypton.packet.transformers.PacketDecoder
import org.kryptonmc.krypton.packet.transformers.PacketEncoder
import org.kryptonmc.krypton.packet.transformers.SizeDecoder
import org.kryptonmc.krypton.packet.transformers.SizeEncoder
import org.kryptonmc.krypton.util.concurrent.NamedThreadFactory
import org.kryptonmc.krypton.util.logger
import java.io.IOException
import kotlin.system.exitProcess

/**
 * The base Netty connection handler initialiser.
 */
class NettyProcess(private val server: KryptonServer) {

    private val bossGroup: EventLoopGroup = bestLoopGroup()
    private val workerGroup: EventLoopGroup = bestLoopGroup()

    suspend fun run() {
        LOGGER.debug("${bossGroup::class.simpleName} is the chosen one")
        try {
            val bootstrap = ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(bestChannel())
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline()
                            .addLast("timeout", ReadTimeoutHandler(30))
                            .addLast(LegacyQueryHandler.NETTY_NAME, LegacyQueryHandler(server.config.status))
                            .addLast(SizeDecoder.NETTY_NAME, SizeDecoder())
                            .addLast(PacketDecoder.NETTY_NAME, PacketDecoder())
                            .addLast(SizeEncoder.NETTY_NAME, SizeEncoder())
                            .addLast(PacketEncoder.NETTY_NAME, PacketEncoder())
                            .addLast(ChannelHandler.NETTY_NAME, ChannelHandler(server))
                    }
                })

            withContext(Dispatchers.IO) {
                val future = bootstrap.bind(server.config.server.ip, server.config.server.port).syncUninterruptibly()
                future.channel().closeFuture().syncUninterruptibly()
            }
        } catch (exception: IOException) {
            LOGGER.error("-------------------------------------------------")
            LOGGER.error("FAILED TO BIND TO PORT ${server.config.server.port}!")
            LOGGER.error("Exception: $exception")
            LOGGER.error("Perhaps a server is already running on that port?")
            LOGGER.error("-------------------------------------------------")
            server.stop()
        } finally {
            shutdown()
        }
    }

    fun shutdown() {
        workerGroup.shutdownGracefully()
        bossGroup.shutdownGracefully()
    }

    // Determines the best loop group to use based on what is available on the current operating system
    private fun bestLoopGroup() = when {
        IOUring.isAvailable() -> IOUringEventLoopGroup(0, NamedThreadFactory("Netty IO Uring Worker #%d"))
        Epoll.isAvailable() -> EpollEventLoopGroup(0, NamedThreadFactory("Netty Epoll Worker #%d"))
        KQueue.isAvailable() -> KQueueEventLoopGroup(0, NamedThreadFactory("Netty KQueue Worker #%d"))
        else -> NioEventLoopGroup(0, NamedThreadFactory("Netty NIO Worker #%d"))
    }

    // Determines the best socket channel to use based on what is available on the current operating system
    private fun bestChannel(): Class<out ServerSocketChannel> = when {
        IOUring.isAvailable() -> IOUringServerSocketChannel::class.java
        Epoll.isAvailable() -> EpollServerSocketChannel::class.java
        KQueue.isAvailable() -> KQueueServerSocketChannel::class.java
        else -> NioServerSocketChannel::class.java
    }

    companion object {

        private val LOGGER = logger<KryptonServer>()
    }
}
