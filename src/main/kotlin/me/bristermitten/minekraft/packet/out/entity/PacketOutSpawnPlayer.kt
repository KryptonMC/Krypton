package me.bristermitten.minekraft.packet.out.entity

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entity.Player
import me.bristermitten.minekraft.extension.writeUUID
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket
import me.bristermitten.minekraft.world.toAngle

class PacketOutSpawnPlayer(
    val player: Player
) : PlayPacket(0x04) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(player.id)
        buf.writeUUID(player.uuid)

        buf.writeDouble(player.location.x)
        buf.writeDouble(player.location.y)
        buf.writeDouble(player.location.z)
        buf.writeByte(player.location.yaw.toAngle())
        buf.writeByte(player.location.pitch.toAngle())
    }
}