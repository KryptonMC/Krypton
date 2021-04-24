package org.kryptonmc.krypton.packet.`in`.status

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.StatusPacket

/**
 * Sent when the client pings the server.
 */
class PacketInPing : StatusPacket(0x01) {

    /**
     * The ping payload. Can be anything, but the Notchian client generates this from the
     * time that this was sent
     */
    var payload = -1L; private set

    override fun read(buf: ByteBuf) {
        payload = buf.readLong()
    }
}
