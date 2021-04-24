package org.kryptonmc.krypton.packet.state

import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketInfo

/**
 * A packet in the [play][PacketState.PLAY] state.
 */
open class PlayPacket(id: Int) : Packet {

    override val info = PacketInfo(id, PacketState.PLAY)
}
