package org.kryptonmc.krypton.packet.out.play.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.Attribute
import org.kryptonmc.krypton.entity.AttributeKey
import org.kryptonmc.krypton.extension.writeKey
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeUUID
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

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
                buf.writeByte(it.operation.id)
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