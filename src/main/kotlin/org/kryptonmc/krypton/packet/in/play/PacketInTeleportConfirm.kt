package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.readVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketInTeleportConfirm : PlayPacket(0x00) {

    var teleportId = -1
        private set

    override fun read(buf: ByteBuf) {
        teleportId = buf.readVarInt()
    }
}