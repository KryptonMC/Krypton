package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Updates the client's time. Namely, the age of the world and the time of day. This is used to keep the client
 * in sync with the server and all other clients.
 *
 * @param worldAge the age of the world
 * @param timeOfDay the time of day of the server
 */
class PacketOutTimeUpdate(
    private val worldAge: Long,
    private val timeOfDay: Long
) : PlayPacket(0x4E) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(worldAge)
        buf.writeLong(timeOfDay)
    }
}
