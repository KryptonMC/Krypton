package org.kryptonmc.krypton.channel

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kryptonmc.krypton.Server
import org.kryptonmc.krypton.packet.transformers.PacketDecoder
import org.kryptonmc.krypton.packet.transformers.PacketEncoder
import org.kryptonmc.krypton.packet.transformers.SizeDecoder
import org.kryptonmc.krypton.packet.transformers.SizeEncoder

class NettyProcess(
    private val server: Server,
    private val port: Int
) {

    private val bossGroup: EventLoopGroup = NioEventLoopGroup()
    private val workerGroup: EventLoopGroup = NioEventLoopGroup()

    suspend fun run() {
        try {
            val bootstrap = ServerBootstrap()
            bootstrap.group(bossGroup)
                .channel(NioServerSocketChannel::class.java)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline()
                            .addLast("timeout", ReadTimeoutHandler(30))
                            .addLast(SizeDecoder.NETTY_NAME, SizeDecoder())
                            .addLast(PacketDecoder.NETTY_NAME, PacketDecoder())
                            .addLast(SizeEncoder.NETTY_NAME, SizeEncoder())
                            .addLast(PacketEncoder.NETTY_NAME, PacketEncoder())
                            .addLast("Handler", ChannelHandler(server))
                    }
                })

            withContext(Dispatchers.IO) {
                val f = bootstrap.bind(port).await()
                f.channel().closeFuture().sync()
            }
        } finally {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
        }
    }
}