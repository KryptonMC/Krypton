package org.kryptonmc.krypton.channel

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import me.bardy.komponent.dsl.textComponent
import me.bardy.komponent.dsl.translationComponent
import org.kryptonmc.krypton.Server
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.krypton.Session
import org.kryptonmc.krypton.SessionStorage
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.login.PacketOutDisconnect
import org.kryptonmc.krypton.packet.out.play.ChatPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutChat
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityDestroy
import org.kryptonmc.krypton.packet.state.PacketState
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.TimeoutException

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
        if (session.currentState == PacketState.PLAY) {
            SessionStorage.sessions.asSequence()
                .filter { it != session }
                .filter { it.currentState == PacketState.PLAY }
                .forEach {
                    it.sendPacket(PacketOutEntityDestroy(listOf(session.id)))
                    it.sendPacket(PacketOutPlayerInfo(
                        PacketOutPlayerInfo.PlayerAction.REMOVE_PLAYER,
                        listOf(PacketOutPlayerInfo.PlayerInfo(profile = session.profile))
                    ))
                    it.sendPacket(PacketOutChat(
                        translationComponent("multiplayer.player.left") {
                            text(session.profile.name)
                        },
                        ChatPosition.SYSTEM_MESSAGE,
                        UUID.fromString("00000000-0000-0000-0000-000000000000")
                    ))
                }
            ServerStorage.playerCount.getAndDecrement()
        }
        SessionStorage.sessions -= session
    }

    override fun messageReceived(ctx: ChannelHandlerContext, msg: Packet) {
        if (!ctx.channel().isOpen) return
        session.receive(msg)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if (!ctx.channel().isOpen) return

        if (cause is TimeoutException) {
            val address = ctx.channel().remoteAddress() as InetSocketAddress
            LOGGER.debug("Connection from ${address.address}:${address.port} timed out. Reason:", cause)
            session.sendPacket(PacketOutDisconnect(translationComponent("disconnect.timeout")))
            session.disconnect()
        } else {
            val disconnectReason = translationComponent("disconnect.genericReason") {
                text("Internal Exception: $cause")
            }
            LOGGER.debug("Failed to send packet! Cause: ", cause)
            session.sendPacket(PacketOutDisconnect(disconnectReason))
            ctx.channel().config().isAutoRead = false
        }
    }

    companion object {

        private val LOGGER = logger<ChannelHandler>()
    }
}