package org.kryptonmc.krypton.packet.out.play.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.world.Location
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.toAngle
import org.kryptonmc.krypton.util.writeAngle
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutEntityTeleport(
    private val entityId: Int,
    private val location: Location,
    private val isOnGround: Boolean
) : PlayPacket(0x56) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeDouble(location.x)
        buf.writeDouble(location.y)
        buf.writeDouble(location.z)
        buf.writeAngle(location.yaw.toAngle())
        buf.writeAngle(location.pitch.toAngle())
        buf.writeBoolean(isOnGround)
    }
}
