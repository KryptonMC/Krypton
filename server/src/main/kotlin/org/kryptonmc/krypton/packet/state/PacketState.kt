package org.kryptonmc.krypton.packet.state

import org.kryptonmc.krypton.packet.Packet

enum class PacketState {

    HANDSHAKE,
    STATUS,
    LOGIN,
    PLAY;

    private val packets = mutableMapOf<Int, () -> Packet>()

    fun registerPacketType(id: Int, supplier: () -> Packet) {
        packets.putIfAbsent(id, supplier)
    }

    fun createPacket(id: Int) = packets[id]?.invoke()

    companion object {

        fun fromId(id: Int) = values()[id]
    }
}