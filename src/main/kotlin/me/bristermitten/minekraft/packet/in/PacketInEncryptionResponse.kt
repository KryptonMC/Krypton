package me.bristermitten.minekraft.packet.`in`

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.readVarInt
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.PacketState

class PacketInEncryptionResponse : Packet
{
    override val info = Companion

    companion object : PacketInfo
    {
        override val id = 0x01
        override val state = PacketState.LOGIN
    }


    override fun read(buf: ByteBuf)
    {
        val secretLength = buf.readVarInt()
        val secret = buf.readBytes(secretLength).array()

        val tokenLength = buf.readVarInt()
        val token = buf.readBytes(tokenLength).array()
    }
}
