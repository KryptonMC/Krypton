package org.kryptonmc.krypton.packet

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.extra.kotlin.translatable
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.krypton.packet.session.Session
import org.kryptonmc.krypton.util.logger
import java.net.InetSocketAddress
import java.util.concurrent.TimeoutException

/**
 * This is the handler at the end of the Netty pipeline that does the actual processing of
 * inbound packets (after they have been processed and turned into Packet objects)
 */
class ChannelHandler(private val server: KryptonServer) : SimpleChannelInboundHandler<Packet>() {

    /**
     * The session this handler handles
     */
    internal lateinit var session: Session

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        LOGGER.debug("[+] Channel Connected: ${ctx.channel().remoteAddress()}")
        session = Session(ServerStorage.NEXT_ENTITY_ID.getAndIncrement(), server, ctx.channel())
        server.sessionManager.sessions += session
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        LOGGER.debug("[-] Channel Disconnected: ${ctx.channel().remoteAddress()}")
        server.sessionManager.handleDisconnection(session)
        server.sessionManager.sessions -= session
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Packet) {
        if (!ctx.channel().isOpen) return
        session.handler.handle(msg)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if (!ctx.channel().isOpen) return

        if (cause is TimeoutException) {
            val address = ctx.channel().remoteAddress() as InetSocketAddress
            LOGGER.debug("Connection from ${address.address}:${address.port} timed out. Reason: ", cause)
            session.disconnect(translatable { key("disconnect.timeout") })
        } else {
            val disconnectReason = translatable {
                key("disconnect.genericReason")
                args(text { content("Internal Exception: $cause") })
            }
            LOGGER.debug("Failed to send or received invalid packet! Cause: ", cause)
            session.disconnect(disconnectReason)
            ctx.channel().config().isAutoRead = false
        }
    }

    companion object {

        const val NETTY_NAME = "handler"

        private val LOGGER = logger<ChannelHandler>()
    }
}
