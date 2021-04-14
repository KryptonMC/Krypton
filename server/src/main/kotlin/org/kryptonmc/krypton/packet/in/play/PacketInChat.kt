package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.readString
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sent when the client either sends a chat message, or when they run a command (indicated by the
 * [message] beginning with a `/`)
 *
 * @author Callum Seabrook
 */
class PacketInChat : PlayPacket(0x03) {

    lateinit var message: String private set

    override fun read(buf: ByteBuf) {
        message = buf.readString(256)
    }
}