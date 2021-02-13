package me.bristermitten.minekraft.packet.`in`.status

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.state.PacketState
import me.bristermitten.minekraft.packet.state.StatusPacket

//class PacketInPing : Packet
//{
//    override val info = Companion
//
//    var payload: Long = -1
//        private set
//
//    override fun read(buf: ByteBuf)
//    {
//        payload = buf.readLong()
//    }
//
//    companion object : PacketInfo
//    {
//        override val id = 0x01
//        override val state = PacketState.STATUS
//    }
//
//}

class PacketInPing : StatusPacket(0x01) {

    var payload = -1L
        private set

    override fun read(buf: ByteBuf) {
        payload = buf.readLong()
    }
}
