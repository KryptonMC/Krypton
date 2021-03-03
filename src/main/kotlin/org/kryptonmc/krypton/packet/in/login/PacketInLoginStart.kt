package org.kryptonmc.krypton.packet.`in`.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.readString
import org.kryptonmc.krypton.packet.state.LoginPacket

class PacketInLoginStart : LoginPacket(0x00) {

    lateinit var name: String
        private set

    override fun read(buf: ByteBuf) {
        name = buf.readString(16)
    }
}