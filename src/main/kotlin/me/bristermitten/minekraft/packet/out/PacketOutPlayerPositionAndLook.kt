package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket
import me.bristermitten.minekraft.world.Location

class PacketOutPlayerPositionAndLook(
    val location: Location,
    val flags: PositionAndLookFlags = PositionAndLookFlags(),
    val teleportId: Int
) : PlayPacket(0x34) {

    override fun write(buf: ByteBuf) {
        buf.writeDouble(location.x)
        buf.writeDouble(location.y)
        buf.writeDouble(location.z)
        buf.writeFloat(location.yaw)
        buf.writeFloat(location.pitch)
        buf.writeByte(flags.toProtocol())
        buf.writeVarInt(teleportId)
    }
}

data class PositionAndLookFlags(
    val isRelativeX: Boolean = false,
    val isRelativeY: Boolean = false,
    val isRelativeZ: Boolean = false,
    val isRelativeYaw: Boolean = false,
    val isRelativePitch: Boolean = false
) {

    fun toProtocol(): Int {
        var flagsByte = 0x00
        if (isRelativeX) flagsByte += 0x01
        if (isRelativeY) flagsByte += 0x02
        if (isRelativeZ) flagsByte += 0x04
        if (isRelativeYaw) flagsByte += 0x08
        if (isRelativePitch) flagsByte += 0x10
        return flagsByte
    }
}