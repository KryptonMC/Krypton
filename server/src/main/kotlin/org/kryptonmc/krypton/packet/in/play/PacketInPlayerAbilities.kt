package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketInPlayerAbilities : PlayPacket(0x1A) {

    var isFlying = false
        private set

    override fun read(buf: ByteBuf) {
        val flags = buf.readByte().toInt()
        isFlying = (flags and 2) != 0
    }
}