package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.readVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sent to confirm a [position and look][org.kryptonmc.krypton.packet.out.play.PacketOutPlayerPositionAndLook] packet.
 * Currently ignored by us due to my reluctance to store a value we don't need for no reason.
 *
 * @author Callum Seabrook
 */
class PacketInTeleportConfirm : PlayPacket(0x00) {

    var teleportId = -1; private set

    override fun read(buf: ByteBuf) {
        teleportId = buf.readVarInt()
    }
}