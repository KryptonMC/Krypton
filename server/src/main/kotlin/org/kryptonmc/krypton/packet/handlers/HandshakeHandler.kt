package org.kryptonmc.krypton.packet.handlers

import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.extra.kotlin.translatable
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.krypton.api.event.events.handshake.HandshakeEvent
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.handshake.PacketInHandshake
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.session.Session
import java.net.InetSocketAddress

class HandshakeHandler(
    override val server: KryptonServer,
    override val session: Session
) : PacketHandler {

    override fun handle(packet: Packet) {
        if (packet !is PacketInHandshake) return // ignore if not a handshake packet
        server.eventBus.call(HandshakeEvent(session.channel.remoteAddress() as InetSocketAddress))

        when (val nextState = packet.data.nextState) {
            PacketState.LOGIN -> handleLogin(packet)
            PacketState.STATUS -> handleStatus()
            else -> throw UnsupportedOperationException("Invalid next state $nextState")
        }
    }

    private fun handleLogin(packet: PacketInHandshake) {
        session.currentState = PacketState.LOGIN
        if (packet.data.protocol != ServerInfo.PROTOCOL) {
            val key = when {
                packet.data.protocol < ServerInfo.PROTOCOL -> "multiplayer.disconnect.outdated_client"
                packet.data.protocol > ServerInfo.PROTOCOL -> "multiplayer.disconnect.outdated_server"
                else -> "multiplayer.disconnect.incompatible"
            }
            val reason = translatable {
                key(key)
                args(text { content(ServerInfo.VERSION) })
            }
            session.disconnect(reason)
            return
        }
        if (ServerStorage.PLAYER_COUNT.get() >= server.config.status.maxPlayers) {
            session.disconnect(translatable { key("multiplayer.disconnect.server_full") })
            return
        }
        session.handler = LoginHandler(server, server.sessionManager, session)
    }

    private fun handleStatus() {
        session.currentState = PacketState.STATUS
        session.handler = StatusHandler(server, session)
    }
}