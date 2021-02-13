package me.bristermitten.minekraft.packet.`in`

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.state.PlayPacket
import me.bristermitten.minekraft.world.Location

class PacketInPlayerPosition : PlayPacket(0x12) {

    lateinit var location: Location
        private set

    var onGround = false
        private set

    override fun read(buf: ByteBuf) {
        val x = buf.readDouble()
        val y = buf.readDouble()
        val z = buf.readDouble()
        location = Location(x, y, z)
        onGround = buf.readBoolean()
    }
}