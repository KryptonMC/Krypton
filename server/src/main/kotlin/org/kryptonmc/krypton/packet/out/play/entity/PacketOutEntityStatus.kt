package org.kryptonmc.krypton.packet.out.play.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sent to indicate a status for an entity.
 *
 * This is currently hard coded to always use the status code 28 (OP level 4 for players), and is only used with players.
 *
 * @param entityId the ID of the entity to set the status for
 */
// TODO: Add the rest of the entity statuses into this
class PacketOutEntityStatus(private val entityId: Int) : PlayPacket(0x1A) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(entityId)
        buf.writeByte(28)
    }
}
