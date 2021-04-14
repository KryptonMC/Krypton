package org.kryptonmc.krypton.packet.`in`.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.readVarIntByteArray
import org.kryptonmc.krypton.packet.state.LoginPacket

/**
 * Sent by the client to inform the server of the shared secret to be used for encryption.
 *
 * Also returns the verify token sent by the server, as verification that we are actually talking to
 * the same client we were talking to before.
 *
 * @author Alex Wood
 */
class PacketInEncryptionResponse : LoginPacket(0x01) {

    lateinit var secret: ByteArray
        private set

    lateinit var verifyToken: ByteArray
        private set

    override fun read(buf: ByteBuf) {
        secret = buf.readVarIntByteArray()
        verifyToken = buf.readVarIntByteArray()
    }
}