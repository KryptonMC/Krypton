package org.kryptonmc.krypton.packet.out.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.packet.state.LoginPacket
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt
import java.security.PublicKey

/**
 * Sent to instruct the client that we wish to encrypt this connection. The client is provided
 * with our public key, so they can use it to encrypt the shared secret, and a verify token,
 * to attempt to ensure the connection hasn't been tampered with (no hackers listening in)
 */
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
