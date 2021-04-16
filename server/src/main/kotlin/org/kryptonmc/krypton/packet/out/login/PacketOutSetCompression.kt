package org.kryptonmc.krypton.packet.out.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.LoginPacket

/**
 * Instructs the client to set a threshold for packet compression. Any packets that are >= [threshold] in size
 * will be compressed.
 *
 * @author Callum Seabrook
 */
class PacketOutSetCompression(private val threshold: Int) : LoginPacket(0x03) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(threshold)
    }
}