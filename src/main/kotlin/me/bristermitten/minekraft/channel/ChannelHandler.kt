package me.bristermitten.minekraft.channel

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import me.bristermitten.minekraft.Server
import me.bristermitten.minekraft.Session
import me.bristermitten.minekraft.SessionStorage
import me.bristermitten.minekraft.packet.Packet

class ChannelHandler(private val server: Server) : SimpleChannelInboundHandler<Packet>()
{

    lateinit var session: Session
        private set

    override fun handlerAdded(ctx: ChannelHandlerContext)
    {
        println("[+] Channel Connected: ${ctx.channel().remoteAddress()}")
        session = Session(ctx.channel(), server)
        SessionStorage.sessions += session
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext)
    {
        println("[-] Channel Disconnected: ${ctx.channel().remoteAddress()}")
        SessionStorage.sessions -= session
    }


    override fun messageReceived(ctx: ChannelHandlerContext, msg: Packet)
    {
        session.receive(msg)
    }
}
