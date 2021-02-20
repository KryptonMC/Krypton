package org.kryptonmc.krypton.channel

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.kryptonmc.krypton.Server
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.krypton.Session
import org.kryptonmc.krypton.SessionStorage
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.state.PacketState

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