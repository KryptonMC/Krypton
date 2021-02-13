package me.bristermitten.minekraft.packet.`in`

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketInPlayerRotation : PlayPacket(0x14) {

    var yaw = 0.0f
        private set

    var pitch = 0.0f
        private set

    var onGround = false
        private set

    override fun read(buf: ByteBuf) {
        yaw = buf.readFloat()
        pitch = buf.readFloat()
        onGround = buf.readBoolean()
    }
}