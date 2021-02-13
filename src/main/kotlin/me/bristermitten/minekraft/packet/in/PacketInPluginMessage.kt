package me.bristermitten.minekraft.packet.`in`

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.readString
import me.bristermitten.minekraft.extension.readVarIntByteArray
import me.bristermitten.minekraft.packet.state.PlayPacket

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