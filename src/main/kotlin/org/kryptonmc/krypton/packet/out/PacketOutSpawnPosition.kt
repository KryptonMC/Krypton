package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.space.Position

class PacketOutSpawnPosition(
    val position: Position
) : PlayPacket(0x42) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(position.toProtocol())
    }
}