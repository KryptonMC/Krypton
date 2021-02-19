package me.bristermitten.minekraft.packet.out.entity

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entity.metadata.EntityMetadata
import me.bristermitten.minekraft.extension.writeUByte
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutEntityMetadata(
    private val entityId: Int,
    private val metadata: EntityMetadata
) : PlayPacket(0x44) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        metadata.write(buf)
        buf.writeUByte(0xFFu) // signals end of metadata
    }
}