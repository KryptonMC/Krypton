package org.kryptonmc.krypton.packet.`in`.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.readVarIntByteArray
import org.kryptonmc.krypton.packet.state.LoginPacket

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