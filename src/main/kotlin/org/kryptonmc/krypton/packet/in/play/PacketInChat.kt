package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.readString
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketInChat : PlayPacket(0x03) {

    lateinit var message: String
        private set

    override fun read(buf: ByteBuf) {
        message = buf.readString(256)
    }
}