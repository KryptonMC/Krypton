package org.kryptonmc.krypton.packet.out.play.entity.spawn

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.Entity
import org.kryptonmc.krypton.space.toAngle
import org.kryptonmc.krypton.extension.writeAngle
import org.kryptonmc.krypton.extension.writeUUID
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Spawn an entity for the client.
 *
 * @param entity the entity to spawn. Will eventually be replaced with the API entity.
 *
 * @author Callum Seabrook
 */
// TODO: Make use of this and use API entity rather than whatever the hell this is
class PacketOutSpawnEntity(private val entity: Entity) : PlayPacket(0x00) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entity.id)
        buf.writeUUID(entity.uuid)
        buf.writeVarInt(entity.type.type)

        buf.writeDouble(entity.location.x)
        buf.writeDouble(entity.location.y)
        buf.writeDouble(entity.location.z)
        buf.writeAngle(entity.location.yaw.toAngle())
        buf.writeAngle(entity.location.pitch.toAngle())

        buf.writeInt(entity.data)

        buf.writeShort(entity.velocityX.toInt())
        buf.writeShort(entity.velocityY.toInt())
        buf.writeShort(entity.velocityZ.toInt())
    }
}