package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarInt

/**
 * Sent when the client attempts to tab complete a command
 */
class PacketInTabComplete : PlayPacket(0x06) {

    /**
     * A unique ID for the tab complete session. Must be returned to the client.
     */
    var id = -1; private set

    /**
     * The command to tab complete. May or may not include '/'.
     */
    lateinit var command: String private set

    override fun read(buf: ByteBuf) {
        id = buf.readVarInt()
        command = buf.readString(32500)
    }
}
