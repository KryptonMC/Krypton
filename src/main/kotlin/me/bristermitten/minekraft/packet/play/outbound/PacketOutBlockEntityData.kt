package me.bristermitten.minekraft.packet.play.outbound

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entities.block.BlockActionUpdate
import me.bristermitten.minekraft.entities.block.BlockPosition
import me.bristermitten.minekraft.packet.play.PlayPacket

class PacketOutBlockEntityData(
    val position: BlockPosition,
    val action: BlockActionUpdate,
    val nbtData: Array<Byte> // replace this with NBT stuff later
) : PlayPacket(0x0A) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(position.toLong())
        buf.writeByte(action.id)
        buf.writeBytes(nbtData.toByteArray())
    }
}