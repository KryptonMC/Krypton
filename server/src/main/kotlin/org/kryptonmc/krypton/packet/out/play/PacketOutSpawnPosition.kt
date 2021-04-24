package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.util.toProtocol
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sets the spawn position for the client. This is not what it may seem, this is the centre of the compass.
 *
 * @param position the spawn position of the client
 */
class PacketOutSpawnPosition(private val position: Location) : PlayPacket(0x42) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(position.toProtocol())
    }
}
