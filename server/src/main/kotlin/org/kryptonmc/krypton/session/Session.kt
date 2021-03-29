package org.kryptonmc.krypton.session

import io.netty.channel.Channel
import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.data.ClientSettings
import org.kryptonmc.krypton.packet.state.PacketState
import kotlin.random.Random

class Session(val id: Int, internal val channel: Channel) {

    lateinit var profile: GameProfile
    lateinit var settings: ClientSettings

    lateinit var player: KryptonPlayer

    var lastKeepAliveId = 0L
    var latency = 0

    @Volatile
    internal var currentState: PacketState = PacketState.HANDSHAKE

    fun sendPacket(packet: Packet) {
        channel.writeAndFlush(packet)
    }

    fun disconnect() {
        if (channel.isOpen) channel.close().awaitUninterruptibly()
    }
}