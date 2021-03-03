package org.kryptonmc.krypton.packet.out.play.entity.spawn

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.space.toAngle
import org.kryptonmc.krypton.entity.entities.Player
import org.kryptonmc.krypton.extension.writeAngle
import org.kryptonmc.krypton.extension.writeUUID
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutSpawnPlayer(val player: Player) : PlayPacket(0x04) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(player.id)
        buf.writeUUID(player.uuid)

        buf.writeDouble(player.location.x)
        buf.writeDouble(player.location.y)
        buf.writeDouble(player.location.z)
        buf.writeAngle(player.location.yaw.toAngle())
        buf.writeAngle(player.location.pitch.toAngle())
    }
}