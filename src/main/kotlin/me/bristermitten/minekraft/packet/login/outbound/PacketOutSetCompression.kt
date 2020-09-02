package me.bristermitten.minekraft.packet.login.outbound

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.login.LoginPacket

class PacketOutSetCompression(
    val threshold: Int
) : LoginPacket(0x03) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(threshold) // the maximum packet size before packets are compressed
    }
}