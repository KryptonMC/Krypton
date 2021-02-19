package me.bristermitten.minekraft.packet.out.entity

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entity.Attribute
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.extension.writeUUID
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutEntityProperties(
    val entityId: Int,
    val properties: List<Attribute> = emptyList()
) : PlayPacket(0x58) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeInt(properties.size)

        properties.forEach { property ->
            buf.writeString(property.key.key.toString())
            buf.writeDouble(property.value)

            buf.writeVarInt(property.modifiers.size)
            property.modifiers.forEach {
                buf.writeUUID(it.uuid)
                buf.writeDouble(it.amount)
                buf.writeByte(it.operation.id)
            }
        }
    }
}