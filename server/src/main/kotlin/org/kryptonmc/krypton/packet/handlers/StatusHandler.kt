package org.kryptonmc.krypton.packet.handlers

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.status.PacketInPing
import org.kryptonmc.krypton.packet.`in`.status.PacketInStatusRequest
import org.kryptonmc.krypton.packet.data.PlayerInfo
import org.kryptonmc.krypton.packet.data.Players
import org.kryptonmc.krypton.packet.data.ServerVersion
import org.kryptonmc.krypton.packet.data.StatusResponse
import org.kryptonmc.krypton.packet.out.status.PacketOutPong
import org.kryptonmc.krypton.packet.out.status.PacketOutStatusResponse
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.session.Session
import org.kryptonmc.krypton.session.SessionManager

/**
 * Handles all inbound packets in the [Status][org.kryptonmc.krypton.packet.state.PacketState.STATUS] state.
 *
 * There are two packets in this state that we handle:
 * - [Status request][org.kryptonmc.krypton.packet.in.status.PacketInStatusRequest] -
 *   sent by the client to request status information
 * - [Ping][org.kryptonmc.krypton.packet.in.status.PacketInPing] -
 *   pings the server (to calculate latency on its end)
 *
 * @author Callum Seabrook
 */
class StatusHandler(
    override val server: KryptonServer,
    override val session: Session
) : PacketHandler {

    override fun handle(packet: Packet) = when (packet) {
        is PacketInStatusRequest -> handleStatusRequest()
        is PacketInPing -> handlePing(packet)
        else -> Unit
    }

    private fun handleStatusRequest() {
        val players = server.sessionManager.sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .map { PlayerInfo(it.profile.name, it.profile.uuid) }
            .toSet()

        session.sendPacket(PacketOutStatusResponse(StatusResponse(
            ServerVersion(ServerInfo.VERSION, ServerInfo.PROTOCOL),
            Players(server.status.maxPlayers, ServerStorage.PLAYER_COUNT.get(), players),
            server.status.motd
        )))
    }

    private fun handlePing(packet: PacketInPing) = session.sendPacket(PacketOutPong(packet.payload))
}