package me.bristermitten.minekraft.packet.login.outbound

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.login.LoginPacket
import java.security.PublicKey


class PacketOutEncryptionRequest(
        private val publicKey: PublicKey,
        private val verifyToken: ByteArray
) : LoginPacket(0x01) {

    companion object {
        /**
         * Will always be empty in the modern protocol
         */
        const val SERVER_ID = ""
    }

    override fun write(buf: ByteBuf) {
        buf.writeString(SERVER_ID)

        val encoded = publicKey.encoded
        buf.writeVarInt(encoded.size)
        buf.writeBytes(encoded)

        buf.writeVarInt(verifyToken.size)
        buf.writeBytes(verifyToken)
    }
}
