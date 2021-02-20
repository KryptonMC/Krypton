package org.kryptonmc.krypton.packet.out.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.metadata.EntityMetadata
import org.kryptonmc.krypton.extension.writeUByte
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

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