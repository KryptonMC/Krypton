package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.state.LoginPacket
import me.bristermitten.minekraft.packet.state.PacketState
import java.util.*

class PacketOutLoginSuccess(
        private val uuid: UUID,
        private val username: String
) : LoginPacket(0x02)
{

    override fun write(buf: ByteBuf)
    {
        buf.writeString(uuid.toString())
        buf.writeString(username)
    }
}
