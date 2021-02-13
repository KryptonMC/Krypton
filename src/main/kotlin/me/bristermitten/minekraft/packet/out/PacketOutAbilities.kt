package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutAbilities : PlayPacket(0x30) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(0x01 + 0x02 + 0x04 + 0x08)
        buf.writeFloat(DEFAULT_FLYING_SPEED)
        buf.writeFloat(DEFAULT_FIELD_OF_VIEW_MODIFIER)
    }

    companion object {

        private const val DEFAULT_FLYING_SPEED = 0.05f
        private const val DEFAULT_FIELD_OF_VIEW_MODIFIER = 0.1f
    }
}