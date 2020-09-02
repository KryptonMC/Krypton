package me.bristermitten.minekraft.packet.play

import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.state.PacketState

open class PlayPacket(val id: Int) : Packet {

    override val info = object : PacketInfo {

        override val id = this@PlayPacket.id
        override val state = PacketState.PLAY
    }
}
