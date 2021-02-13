package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket
import me.bristermitten.minekraft.world.Location

class PacketOutPlayerPositionAndLook(val location: Location) : PlayPacket(0x34) {

    override fun write(buf: ByteBuf) {
        buf.writeDouble(location.x)
        buf.writeDouble(location.y)
        buf.writeDouble(location.z)
        buf.writeFloat(location.yaw)
        buf.writeFloat(location.pitch)
        buf.writeByte(0)
        buf.writeVarInt(0)

//        buf.writeDouble(0.0)
//        buf.writeDouble(0.0)
//        buf.writeDouble(0.0)
//        buf.writeFloat(0f)
//        buf.writeFloat(0f)
//        buf.writeByte(0)
//        buf.writeVarInt(0)
    }
}
