package org.kryptonmc.krypton.packet.out.play.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Tells the client to destroy all of the entities with IDs in the given list of [entityIds]
 *
 * @param entityIds the list of entity IDs to destroy
 */
class PacketOutEntityDestroy(private val entityIds: List<Int>) : PlayPacket(0x36) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityIds.size)
        entityIds.forEach { buf.writeVarInt(it) }
    }
}
