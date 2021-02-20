package org.kryptonmc.krypton.packet.`in`

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.readString
import org.kryptonmc.krypton.extension.readVarIntByteArray
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketInPluginMessage : PlayPacket(0x0B) {

    lateinit var channel: String
        private set

    lateinit var data: ByteArray
        private set

    override fun read(buf: ByteBuf) {
        channel = buf.readString()
        data = buf.readVarIntByteArray()
    }
}