package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.extension.toProtocol
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutSpawnPosition(private val position: Location) : PlayPacket(0x42) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(position.toProtocol())
    }
}