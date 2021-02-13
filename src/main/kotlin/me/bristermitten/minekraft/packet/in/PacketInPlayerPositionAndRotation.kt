package me.bristermitten.minekraft.packet.`in`

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.state.PlayPacket
import me.bristermitten.minekraft.world.Location

class PacketInPlayerPositionAndRotation : PlayPacket(0x13) {

    lateinit var location: Location
        private set

    var onGround = false
        private set

    override fun read(buf: ByteBuf) {
        val x = buf.readDouble()
        val y = buf.readDouble()
        val z = buf.readDouble()
        val yaw = buf.readFloat()
        val pitch = buf.readFloat()
        location = Location(x, y, z, yaw, pitch)
        onGround = buf.readBoolean()
    }
}