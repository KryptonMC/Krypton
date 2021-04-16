package org.kryptonmc.krypton.session

import io.netty.channel.Channel
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.data.ClientSettings
import org.kryptonmc.krypton.packet.handlers.HandshakeHandler
import org.kryptonmc.krypton.packet.handlers.PacketHandler
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect
import org.kryptonmc.krypton.packet.out.play.PacketOutDisconnect
import org.kryptonmc.krypton.packet.state.PacketState

/**
 * Represents a session, which is a connection between the server and a client.
 *
 * @param id the entity ID of this session
 * @param server the server this session is connected to
 * @param channel the backing Netty channel that does all the IO stuff
 *
 * @author Alex Wood
 * @author Callum Seabrook
 */
class Session(
    val id: Int,
    server: KryptonServer,
    internal val channel: Channel
) {

    lateinit var profile: GameProfile
    lateinit var settings: ClientSettings

    lateinit var player: KryptonPlayer

    var lastKeepAliveId = 0L
    var latency = 0

    @Volatile
    internal var currentState: PacketState = PacketState.HANDSHAKE

    @Volatile
    internal var handler: PacketHandler = HandshakeHandler(server, this)

    fun sendPacket(packet: Packet) {
        channel.writeAndFlush(packet)
    }

    fun disconnect(reason: Component) {
        when (currentState) {
            PacketState.PLAY -> sendPacket(PacketOutDisconnect(reason))
            PacketState.LOGIN -> sendPacket(PacketOutLoginDisconnect(reason))
            else -> Unit
        }
        if (channel.isOpen) channel.close().awaitUninterruptibly()
    }
}