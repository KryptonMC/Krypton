package org.kryptonmc.krypton.channel

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.extra.kotlin.translatable
import org.kryptonmc.krypton.*
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.login.PacketOutDisconnect
import org.kryptonmc.krypton.packet.out.play.ChatPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutChat
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityDestroy
import org.kryptonmc.krypton.packet.state.PacketState
import java.net.InetSocketAddress
import java.util.concurrent.TimeoutException

class ChannelHandler(private val server: Server) : SimpleChannelInboundHandler<Packet>() {

    lateinit var session: Session
        private set

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        LOGGER.debug("[+] Channel Connected: ${ctx.channel().remoteAddress()}")
        session = Session(ServerStorage.NEXT_ENTITY_ID.getAndIncrement(), ctx.channel(), server)
        SessionStorage.sessions += session
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        LOGGER.debug("[-] Channel Disconnected: ${ctx.channel().remoteAddress()}")
        if (session.currentState == PacketState.PLAY) {
            val destroyPacket = PacketOutEntityDestroy(listOf(session.id))
            val infoPacket = PacketOutPlayerInfo(
                PacketOutPlayerInfo.PlayerAction.REMOVE_PLAYER,
                listOf(PacketOutPlayerInfo.PlayerInfo(profile = session.profile))
            )
            val leavePacket = PacketOutChat(
                translatable {
                    key("multiplayer.player.left")
                    args(text { content(session.profile.name) })
                },
                ChatPosition.SYSTEM_MESSAGE,
                SERVER_UUID
            )

            SessionStorage.sessions.asSequence()
                .filter { it != session }
                .filter { it.currentState == PacketState.PLAY }
                .forEach {
                    it.sendPacket(destroyPacket)
                    it.sendPacket(infoPacket)
                    it.sendPacket(leavePacket)
                }
            ServerStorage.PLAYER_COUNT.getAndDecrement()
        }
        SessionStorage.sessions -= session
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Packet) {
        if (!ctx.channel().isOpen) return
        session.receive(msg)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if (!ctx.channel().isOpen) return

        if (cause is TimeoutException) {
            val address = ctx.channel().remoteAddress() as InetSocketAddress
            LOGGER.debug("Connection from ${address.address}:${address.port} timed out. Reason: ", cause)
            session.sendPacket(PacketOutDisconnect(translatable { key("disconnect.timeout") }))
            session.disconnect()
        } else {
            val disconnectReason = translatable {
                key("disconnect.genericReason")
                args(text { content("Internal Exception: $cause") })
            }
            LOGGER.debug("Failed to send packet! Cause: ", cause)
            session.sendPacket(PacketOutDisconnect(disconnectReason))
            ctx.channel().config().isAutoRead = false
        }
    }

    companion object {

        const val NETTY_NAME = "handler"

        private val LOGGER = logger<ChannelHandler>()
    }
}