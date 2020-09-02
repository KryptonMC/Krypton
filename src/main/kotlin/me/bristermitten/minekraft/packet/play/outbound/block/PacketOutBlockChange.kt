package me.bristermitten.minekraft.packet.play.outbound.block

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entities.block.Block
import me.bristermitten.minekraft.entities.block.BlockPosition
import me.bristermitten.minekraft.packet.play.PlayPacket

class PacketOutBlockChange(
    val position: BlockPosition,
    val block: Block
) : PlayPacket(0x0C) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(position.toLong())
    }
}