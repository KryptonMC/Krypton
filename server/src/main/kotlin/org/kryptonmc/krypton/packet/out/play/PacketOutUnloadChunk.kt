package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.chunk.ChunkPosition

class PacketOutUnloadChunk(private val position: ChunkPosition) : PlayPacket(0x1C) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(position.x)
        buf.writeInt(position.z)
    }
}