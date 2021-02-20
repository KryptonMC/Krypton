package org.kryptonmc.krypton.packet.`in`.handshake

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.readString
import org.kryptonmc.krypton.extension.readVarInt
import org.kryptonmc.krypton.extension.toInetAddress
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketInfo
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.packet.data.HandshakeData

class PacketInHandshake : Packet {

    override val info = object : PacketInfo {
        override val id = 0x00
        override val state = PacketState.HANDSHAKE
    }

    lateinit var data: HandshakeData
        private set

    override fun read(buf: ByteBuf) {
        val protocol = buf.readVarInt()
        val address = buf.readString(255).toInetAddress()
        val port = buf.readShort()
        val nextState = PacketState.fromId(buf.readVarInt())

        data = HandshakeData(protocol, address, port, nextState)
    }
}