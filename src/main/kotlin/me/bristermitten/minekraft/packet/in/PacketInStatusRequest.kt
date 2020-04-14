package me.bristermitten.minekraft.packet.`in`

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.PacketState

class PacketInStatusRequest : Packet
{
    override val info: PacketInfo = Companion

    companion object : PacketInfo
    {
        override val id = 0x00
        override val state = PacketState.STATUS
    }

    override fun read(buf: ByteBuf)
    {
        //No data in packet
    }
}
