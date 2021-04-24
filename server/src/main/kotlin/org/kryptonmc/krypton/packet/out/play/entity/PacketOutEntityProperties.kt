package org.kryptonmc.krypton.packet.out.play.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.Attribute
import org.kryptonmc.krypton.entity.AttributeKey
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Set entity attributes. Note that this packet is not incremental, and sending it with data will reset
 * all of the existing attributes and reapply them.
 *
 * @param entityId the ID of the entity to set the attributes of
 * @param properties the attributes to set
 */
class PacketOutEntityProperties(
    private val entityId: Int,
    private val properties: Collection<Attribute> = emptyList()
) : PlayPacket(0x58) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeInt(properties.size)

        properties.forEach { property ->
            buf.writeKey(property.key.key)
            buf.writeDouble(property.value)

            buf.writeVarInt(property.modifiers.size)
            property.modifiers.forEach {
                buf.writeUUID(it.uuid)
                buf.writeDouble(it.amount)
                buf.writeByte(it.operation.ordinal)
            }
        }
    }

    companion object {

        val DEFAULT_PLAYER_ATTRIBUTES = setOf(
            Attribute(AttributeKey.GENERIC_MAX_HEALTH, 20.0),
            Attribute(AttributeKey.GENERIC_MOVEMENT_SPEED, 0.1)
        )
    }
}
