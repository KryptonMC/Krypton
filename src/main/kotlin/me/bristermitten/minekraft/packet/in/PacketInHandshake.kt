package me.bristermitten.minekraft.packet.`in`

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.readString
import me.bristermitten.minekraft.extension.readVarInt
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.PacketState
import me.bristermitten.minekraft.packet.data.HandshakeData

class PacketInHandshake : Packet
{
    override val info = object : PacketInfo
    {
        override val id = 0x0
        override val state = PacketState.HANDSHAKE
    }

    lateinit var data: HandshakeData
        private set

    override fun read(buf: ByteBuf)
    {
        data = HandshakeData()
        data.protocol = buf.readVarInt()
        data.address = buf.readString(255)
        data.port = buf.readShort()
        data.nextState = buf.readVarInt()
    }
}

