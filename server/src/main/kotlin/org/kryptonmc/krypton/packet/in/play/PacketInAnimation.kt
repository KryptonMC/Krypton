package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.Hand
import org.kryptonmc.krypton.extension.readVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketInAnimation : PlayPacket(0x2C) {

    lateinit var hand: Hand
        private set

    override fun read(buf: ByteBuf) {
        hand = Hand.fromId(buf.readVarInt())
    }
}