package org.kryptonmc.krypton.packet.state

import org.kryptonmc.krypton.packet.Packet

enum class PacketState(val id: Int) {

    HANDSHAKE(0),
    STATUS(1),
    LOGIN(2),
    PLAY(3);

    private val packets: MutableMap<Int, () -> Packet> = mutableMapOf()

    fun registerPacketType(id: Int, supplier: () -> Packet) {
        packets.putIfAbsent(id, supplier)
    }

    fun createPacket(id: Int): Packet? {
        val packetCreator = packets[id]
        return packetCreator?.invoke()
    }

    companion object {

        private val VALUES = values()

        fun fromId(id: Int) = VALUES[id]
    }
}