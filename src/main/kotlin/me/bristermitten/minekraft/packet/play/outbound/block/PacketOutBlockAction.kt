package me.bristermitten.minekraft.packet.play.outbound.block

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entities.block.BlockPosition
import me.bristermitten.minekraft.entities.block.actions.BlockAction
import me.bristermitten.minekraft.entities.block.actions.BlockType
import me.bristermitten.minekraft.packet.play.PlayPacket

class PacketOutBlockAction(
    val position: BlockPosition,
    val action: BlockAction<BlockAction.ActionID, BlockAction.ActionParameter>,
    val type: BlockType
) : PlayPacket(0x0B) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(position.toLong())

        action.actionID?.id?.let { buf.writeByte(it) }
        action.actionValue?.id?.let { buf.writeByte(it) }

        buf.writeInt(type.id)
    }
}