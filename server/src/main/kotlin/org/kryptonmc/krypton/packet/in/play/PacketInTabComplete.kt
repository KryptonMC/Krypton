package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.readString
import org.kryptonmc.krypton.extension.readVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketInTabComplete : PlayPacket(0x06) {

    var id = -1; private set
    lateinit var command: String private set

    override fun read(buf: ByteBuf) {
        id = buf.readVarInt()
        command = buf.readString(32500)
    }
}