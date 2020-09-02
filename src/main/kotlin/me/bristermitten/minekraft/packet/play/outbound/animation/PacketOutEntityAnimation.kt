package me.bristermitten.minekraft.packet.play.outbound.animation

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entities.AnimationType
import me.bristermitten.minekraft.packet.play.outbound.EntityPacket
import me.bristermitten.minekraft.packet.play.outbound.EntityPacketType

class PacketOutEntityAnimation(
    override val entityID: Int,
    val type: AnimationType
) : EntityPacket(EntityPacketType.ENTITY_ANIMATION) {

    override fun write(buf: ByteBuf) {
        super.write(buf)
        buf.writeByte(type.id)
    }
}