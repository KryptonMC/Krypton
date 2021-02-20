package org.kryptonmc.krypton.packet.state

import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketInfo

open class PlayPacket(val id: Int) : Packet {

    override val info = object : PacketInfo {
        override val id = this@PlayPacket.id
        override val state = PacketState.PLAY
    }
}