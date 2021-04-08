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