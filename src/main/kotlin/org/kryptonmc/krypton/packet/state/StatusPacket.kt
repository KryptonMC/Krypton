package org.kryptonmc.krypton.packet.state

import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketInfo

open class StatusPacket(val id: Int) : Packet {

    override val info = object : PacketInfo {
        override val id = this@StatusPacket.id
        override val state = PacketState.STATUS
    }
}