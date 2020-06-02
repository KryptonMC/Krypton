package me.bristermitten.minekraft.packet.`in`

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.state.PacketState

class PacketInPing : Packet
{
    override val info = Companion

    var payload: Long = -1
        private set

    override fun read(buf: ByteBuf)
    {
        payload = buf.readLong()
    }

    companion object : PacketInfo
    {
        override val id = 0x01
        override val state = PacketState.STATUS
    }

}
