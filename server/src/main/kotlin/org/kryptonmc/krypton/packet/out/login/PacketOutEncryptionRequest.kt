package org.kryptonmc.krypton.packet.out.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.LoginPacket
import java.security.PublicKey

class PacketOutEncryptionRequest(
    private val publicKey: PublicKey,
    private val verifyToken: ByteArray
) : LoginPacket(0x01) {

    override fun write(buf: ByteBuf) {
        buf.writeString(ServerInfo.SERVER_ID, 20)

        val encoded = publicKey.encoded
        buf.writeVarInt(encoded.size)
        buf.writeBytes(encoded)

        buf.writeVarInt(verifyToken.size)
        buf.writeBytes(verifyToken)
    }
}