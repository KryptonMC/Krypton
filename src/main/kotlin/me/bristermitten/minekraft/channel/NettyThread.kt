package me.bristermitten.minekraft.channel

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.bristermitten.minekraft.Server
import me.bristermitten.minekraft.packet.transformers.PacketDecoder
import me.bristermitten.minekraft.packet.transformers.PacketEncoder
import me.bristermitten.minekraft.packet.transformers.SizeDecoder
import me.bristermitten.minekraft.packet.transformers.SizeEncoder

class NettyThread(
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
                            .addLast(SizeDecoder())
                            .addLast(
                                PacketDecoder.NETTY_NAME,
                                PacketDecoder()
                            )

                            .addLast(SizeEncoder())
                            .addLast(
                                PacketEncoder.NETTY_NAME,
                                PacketEncoder()
                            )
                            .addLast("Handler", ChannelHandler(server))
                    }
                })
            withContext(Dispatchers.IO) {
                val f = bootstrap.bind(port).sync()
                f.channel().closeFuture().sync()
            }
        } finally {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
        }
    }
}
