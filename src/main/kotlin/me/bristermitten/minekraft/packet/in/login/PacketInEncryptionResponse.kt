package me.bristermitten.minekraft.packet.`in`.login

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.readVarIntByteArray
import me.bristermitten.minekraft.packet.state.LoginPacket
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


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
