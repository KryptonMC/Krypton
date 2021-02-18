package me.bristermitten.minekraft.channel

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import me.bristermitten.minekraft.Server
import me.bristermitten.minekraft.ServerStorage
import me.bristermitten.minekraft.Session
import me.bristermitten.minekraft.SessionStorage
import me.bristermitten.minekraft.extension.logger
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.state.PacketState

class ChannelHandler(private val server: Server) : SimpleChannelInboundHandler<Packet>() {

    lateinit var session: Session
        private set

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        LOGGER.debug("[+] Channel Connected: ${ctx.channel().remoteAddress()}")
        session = Session(ServerStorage.nextEntityId.getAndIncrement(), ctx.channel(), server)
        SessionStorage.sessions += session
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        LOGGER.debug("[-] Channel Disconnected: ${ctx.channel().remoteAddress()}")
        if (session.currentState == PacketState.PLAY) ServerStorage.playerCount.getAndDecrement()
        SessionStorage.sessions -= session
    }

    override fun messageReceived(ctx: ChannelHandlerContext, msg: Packet) {
        session.receive(msg)
    }

    companion object {

        private val LOGGER = logger<ChannelHandler>()
    }
}