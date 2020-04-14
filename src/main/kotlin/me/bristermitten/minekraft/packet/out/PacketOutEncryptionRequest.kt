package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.PacketState
import java.security.PublicKey

class PacketOutEncryptionRequest(
    private val serverId: String,
    private val publicKey: PublicKey,
    private val verifyToken: ByteArray
) : Packet
{
    override val info = Companion

    companion object : PacketInfo
    {
        override val id = 0x01
        override val state = PacketState.LOGIN
    }

    override fun write(buf: ByteBuf)
    {
        buf.writeString(serverId)

        val encoded = publicKey.encoded
        buf.writeVarInt(encoded.size)
        buf.writeBytes(encoded)

        buf.writeVarInt(verifyToken.size)
        buf.writeBytes(verifyToken)

    }
}
