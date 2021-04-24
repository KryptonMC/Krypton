package org.kryptonmc.krypton.packet.out.play.entity.spawn

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.toAngle
import org.kryptonmc.krypton.util.writeAngle
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Spawn a player for the client.
 *
 * @param player the player to spawn.
 */
class PacketOutSpawnPlayer(private val player: KryptonPlayer) : PlayPacket(0x04) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(player.session.id)
        buf.writeUUID(player.uuid)

        buf.writeDouble(player.location.x)
        buf.writeDouble(player.location.y)
        buf.writeDouble(player.location.z)
        buf.writeAngle(player.location.yaw.toAngle())
        buf.writeAngle(player.location.pitch.toAngle())
    }
}
