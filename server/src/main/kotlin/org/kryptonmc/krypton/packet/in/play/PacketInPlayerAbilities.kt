package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * This is another poorly named packet. This packet is only for indicating when the client begins/ends
 * flying. That's literally it.
 *
 * @author Callum Seabrook
 */
class PacketInPlayerAbilities : PlayPacket(0x1A) {

    var isFlying = false; private set

    override fun read(buf: ByteBuf) {
        val flags = buf.readByte().toInt()
        isFlying = (flags and 2) != 0
    }
}