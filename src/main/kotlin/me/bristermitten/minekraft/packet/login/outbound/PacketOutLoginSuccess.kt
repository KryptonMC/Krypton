package me.bristermitten.minekraft.packet.login.outbound

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.packet.login.LoginPacket
import java.util.*

class PacketOutLoginSuccess(
    private val uuid: UUID,
    private val username: String
) : LoginPacket(0x02) {

    override fun write(buf: ByteBuf) {
        buf.writeString(uuid.toString())
        buf.writeString(username)
    }
}
