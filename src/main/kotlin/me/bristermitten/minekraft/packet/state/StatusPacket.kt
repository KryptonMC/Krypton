package me.bristermitten.minekraft.packet.state

import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo

open class StatusPacket(val id: Int) : Packet {

    override val info = object : PacketInfo {
        override val id = this@StatusPacket.id
        override val state = PacketState.STATUS
    }
}