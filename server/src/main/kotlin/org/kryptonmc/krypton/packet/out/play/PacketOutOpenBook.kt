package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.entity.Hand
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutOpenBook(private val hand: Hand) : PlayPacket(0x15) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(hand.ordinal)
    }
}
