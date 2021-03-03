package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.Location

class PacketOutPlayerPositionAndLook(
    private val location: Location,
    private val flags: PositionAndLookFlags = PositionAndLookFlags(),
    private val teleportId: Int
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