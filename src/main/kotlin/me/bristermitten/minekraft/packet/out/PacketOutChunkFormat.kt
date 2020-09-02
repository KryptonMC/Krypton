package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.play.PlayPacket

class PacketOutChunkFormat : PlayPacket(0x22) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(0)
        buf.writeInt(0)
        buf.writeBoolean(true)

    }
}
