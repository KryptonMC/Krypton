package me.bristermitten.minekraft.packet.`in`

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.readString
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketInChat : PlayPacket(0x03) {

    lateinit var message: String

    override fun read(buf: ByteBuf) {
        message = buf.readString(256)
    }
}