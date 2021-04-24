package org.kryptonmc.krypton.packet.`in`.handshake

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketInfo
import org.kryptonmc.krypton.packet.data.HandshakeData
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.toInetAddress

/**
 * This is the only packet in the [Handshake][PacketState.HANDSHAKE] state.
 *
 * The client uses this packet to inform the server of its intention for the connection (either
 * login or status)
 */
class PacketInHandshake : Packet {

    override val info = PacketInfo(0x00, PacketState.HANDSHAKE)

    /**
     * The data in the handshake
     */
    lateinit var data: HandshakeData private set

    override fun read(buf: ByteBuf) {
        val protocol = buf.readVarInt()
        val address = buf.readString(255).toInetAddress()
        val port = buf.readUnsignedShort().toUShort()
        val nextState = PacketState.fromId(buf.readVarInt())

        data = HandshakeData(protocol, address, port, nextState)
    }
}
