package org.kryptonmc.krypton.packet.`in`.handshake

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.readString
import org.kryptonmc.krypton.extension.readVarInt
import org.kryptonmc.krypton.extension.toInetAddress
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketInfo
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.packet.data.HandshakeData

/**
 * This is the only packet in the [Handshake][PacketState.HANDSHAKE] state.
 *
 * The client uses this packet to inform the server of its intention for the connection (either
 * login or status)
 *
 * @author Alex Wood
 * @author Callum Seabrook
 */
class PacketInHandshake : Packet {

    override val info = PacketInfo(0x00, PacketState.HANDSHAKE)

    lateinit var data: HandshakeData
        private set

    override fun read(buf: ByteBuf) {
        val protocol = buf.readVarInt()
        val address = buf.readString(255).toInetAddress()
        val port = buf.readUnsignedShort().toUShort()
        val nextState = PacketState.fromId(buf.readVarInt())

        data = HandshakeData(protocol, address, port, nextState)
    }
}