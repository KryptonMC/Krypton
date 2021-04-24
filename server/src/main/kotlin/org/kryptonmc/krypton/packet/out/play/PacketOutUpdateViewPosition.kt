package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.chunk.ChunkPosition

/**
 * Update the centre chunk of where the client is viewing.
 *
 * @param position the chunk position of the view point
 */
class PacketOutUpdateViewPosition(private val position: ChunkPosition) : PlayPacket(0x40) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(position.x)
        buf.writeVarInt(position.z)
    }
}
