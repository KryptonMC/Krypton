package me.bristermitten.minekraft.packet.login.inbound

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.readVarIntByteArray
import me.bristermitten.minekraft.packet.login.LoginPacket


class PacketInEncryptionResponse : LoginPacket(0x01)
{

    lateinit var secret: ByteArray
    lateinit var verifyToken: ByteArray

    override fun read(buf: ByteBuf)
    {
        secret = buf.readVarIntByteArray()
        verifyToken = buf.readVarIntByteArray()
    }
}
