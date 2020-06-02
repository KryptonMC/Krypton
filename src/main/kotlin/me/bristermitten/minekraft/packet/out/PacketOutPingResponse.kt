package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.state.PacketState

class PacketOutPingResponse(private val value: Long) : Packet
{

    override val info = Companion

    companion object : PacketInfo
    {
        override val id: Int = 0x01
        override val state = PacketState.STATUS
    }


    override fun write(buf: ByteBuf)
    {
        buf.writeLong(value)
    }
}
