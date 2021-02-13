package me.bristermitten.minekraft.packet.out.status

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.state.PacketState
import me.bristermitten.minekraft.packet.state.StatusPacket

//class PacketOutPong(private val value: Long) : Packet
//{
//
//    override val info = Companion
//
//    companion object : PacketInfo
//    {
//        override val id: Int = 0x01
//        override val state = PacketState.STATUS
//    }
//
//
//    override fun write(buf: ByteBuf)
//    {
//        buf.writeLong(value)
//    }
//}

class PacketOutPong(private val value: Long) : StatusPacket(0x01) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(value)
    }
}
