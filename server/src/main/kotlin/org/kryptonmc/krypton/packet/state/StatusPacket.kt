package org.kryptonmc.krypton.packet.state

import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketInfo

/**
 * Represents a packet in the [status][PacketState.STATUS] state.
 */
open class StatusPacket(id: Int) : Packet {

    override val info = PacketInfo(id, PacketState.STATUS)
}
