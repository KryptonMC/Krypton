package me.bristermitten.minekraft.packet.play.outbound.animation

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entities.block.BlockPosition
import me.bristermitten.minekraft.packet.play.outbound.EntityPacket
import me.bristermitten.minekraft.packet.play.outbound.EntityPacketType

class PacketOutBlockBreakAnimation(
    override val entityID: Int,
    val position: BlockPosition,
    val destroyStage: Int
) : EntityPacket(EntityPacketType.BLOCK_BREAK_ANIMATION) {

    override fun write(buf: ByteBuf) {
        super.write(buf)
        buf.writeLong(position.toLong())
        buf.writeInt(destroyStage)
    }
}