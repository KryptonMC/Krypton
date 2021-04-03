package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.chunk.ChunkPosition

class PacketOutUpdateViewPosition(private val position: ChunkPosition) : PlayPacket(0x40) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(position.x)
        buf.writeVarInt(position.z)
    }
}