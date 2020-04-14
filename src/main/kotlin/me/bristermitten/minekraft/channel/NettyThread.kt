package me.bristermitten.minekraft.channel

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import me.bristermitten.minekraft.packet.codec.PacketDecoder
import me.bristermitten.minekraft.packet.codec.PacketEncoder
import me.bristermitten.minekraft.packet.codec.SizeDecoder
import me.bristermitten.minekraft.packet.codec.SizeEncoder

class NettyThread : Thread()
{
    private val port = 25565
    private val bossGroup: EventLoopGroup = NioEventLoopGroup()
    private val workerGroup: EventLoopGroup = NioEventLoopGroup()
    override fun run()
    {
        try
        {
            val bootstrap = ServerBootstrap()
            bootstrap.group(bossGroup)
                .channel(NioServerSocketChannel::class.java)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(object : ChannelInitializer<SocketChannel>()
                {
                    override fun initChannel(ch: SocketChannel)
                    {
                        ch.pipeline()
                            .addLast(SizeDecoder())
                            .addLast(
                                "Decoder",
                                PacketDecoder()
                            )

                            .addLast(SizeEncoder())
                            .addLast(
                                "Encoder",
                                PacketEncoder()
                            )
                            .addLast("Handler", ChannelHandler())
                    }
                })
            val f: ChannelFuture = bootstrap.bind(port).sync()
            f.channel().closeFuture().sync()
        } finally
        {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
        }
    }
}
