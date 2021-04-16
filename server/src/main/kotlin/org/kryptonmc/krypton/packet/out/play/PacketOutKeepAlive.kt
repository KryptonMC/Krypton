package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * This is a message to the client to say "hey, I'm still here by the way", so the client doesn't just assume
 * we've timed out and disconnect.
 *
 * @param keepAliveId a unique ID for the keep alive. Vanilla calls this the challenge, and this is generally
 * [System.currentTimeMillis]
 *
 * @author Callum Seabrook
 */
class PacketOutKeepAlive(private val keepAliveId: Long) : PlayPacket(0x1F) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(keepAliveId)
    }
}