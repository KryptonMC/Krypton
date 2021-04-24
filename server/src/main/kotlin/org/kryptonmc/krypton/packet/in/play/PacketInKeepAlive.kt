package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sent by the client to confirm to the server that it's still alive.
 */
class PacketInKeepAlive : PlayPacket(0x10) {

    /**
     * This must be identical to the one sent by the server in [org.kryptonmc.krypton.packet.out.play.PacketOutKeepAlive]
     */
    var keepAliveId = 0L; private set

    override fun read(buf: ByteBuf) {
        keepAliveId = buf.readLong()
    }
}
