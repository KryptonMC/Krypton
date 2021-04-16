package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.extension.toProtocol
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sets the spawn position for the client. This is not what it may seem, this is the centre of the compass.
 *
 * @param position the spawn position of the client
 *
 * @author Callum Seabrook
 */
class PacketOutSpawnPosition(private val position: Location) : PlayPacket(0x42) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(position.toProtocol())
    }
}