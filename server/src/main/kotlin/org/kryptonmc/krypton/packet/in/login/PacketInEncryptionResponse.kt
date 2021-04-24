package org.kryptonmc.krypton.packet.`in`.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.LoginPacket
import org.kryptonmc.krypton.util.readVarIntByteArray

/**
 * Sent by the client to inform the server of the shared secret to be used for encryption.
 *
 * Also returns the verify token sent by the server, as verification that we are actually talking to
 * the same client we were talking to before.
 */
class PacketInEncryptionResponse : LoginPacket(0x01) {

    /**
     * The shared secret used for encryption, encrypted with the server's public key
     */
    lateinit var secret: ByteArray private set

    /**
     * The verify token earlier sent by the server, also encrypted with the server's public key
     */
    lateinit var verifyToken: ByteArray private set

    override fun read(buf: ByteBuf) {
        secret = buf.readVarIntByteArray()
        verifyToken = buf.readVarIntByteArray()
    }
}
