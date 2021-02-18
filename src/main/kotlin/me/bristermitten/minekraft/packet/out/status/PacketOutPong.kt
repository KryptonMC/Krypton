package me.bristermitten.minekraft.packet.out.status

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.state.StatusPacket

class PacketOutPong(private val value: Long) : StatusPacket(0x01) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(value)
    }
}