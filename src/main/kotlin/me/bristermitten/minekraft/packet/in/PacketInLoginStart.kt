package me.bristermitten.minekraft.packet.`in`

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.logger
import me.bristermitten.minekraft.extension.readString
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.state.LoginPacket
import me.bristermitten.minekraft.packet.state.PacketState

class PacketInLoginStart : LoginPacket(0x00) {

    lateinit var name: String

    override fun read(buf: ByteBuf) {
        name = buf.readString(16)
    }
}