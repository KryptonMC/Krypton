package me.bristermitten.minekraft.packet.login.inbound

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.readString
import me.bristermitten.minekraft.packet.login.LoginPacket

class PacketInLoginStart : LoginPacket(0x00) {

    override fun read(buf: ByteBuf) {
        buf.readString(16)
    }
}
