package me.bristermitten.minekraft.packet.`in`.handshake

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.readString
import me.bristermitten.minekraft.extension.readVarInt
import me.bristermitten.minekraft.extension.toInetAddress
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.state.PacketState
import me.bristermitten.minekraft.packet.data.HandshakeData

class PacketInHandshake : Packet {

    override val info = object : PacketInfo {
        override val id = 0x00
        override val state = PacketState.HANDSHAKE
    }

    lateinit var data: HandshakeData
        private set

    override fun read(buf: ByteBuf) {
        val protocol = buf.readVarInt()
        val address = buf.readString(255).toInetAddress()
        val port = buf.readShort()
        val nextState = PacketState.fromId(buf.readVarInt())

        data = HandshakeData(protocol, address, port, nextState)
    }
}

