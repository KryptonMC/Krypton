package me.bristermitten.minekraft.packet.play.outbound.spawn

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entities.Direction
import me.bristermitten.minekraft.entities.PaintingType
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.extension.writeUUID
import me.bristermitten.minekraft.packet.play.outbound.EntityPacket
import me.bristermitten.minekraft.packet.play.outbound.EntityPacketType
import java.util.*
import kotlin.math.max

class PacketOutSpawnPainting(
    override val entityID: Int,
    val entityUUID: UUID,
    val type: PaintingType,
    val direction: Direction
) : EntityPacket(EntityPacketType.SPAWN_PAINTING) {

    override fun write(buf: ByteBuf) {
        super.write(buf)
        buf.writeUUID(entityUUID)
        buf.writeInt(type.id)

        buf.writeString("(${max(0, type.width / 2 - 1)}, ${type.height / 2})")
        buf.writeByte(direction.id)
    }
}