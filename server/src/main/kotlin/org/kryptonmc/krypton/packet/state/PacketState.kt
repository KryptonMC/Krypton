package org.kryptonmc.krypton.packet.state

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.kryptonmc.krypton.packet.Packet

enum class PacketState {

    HANDSHAKE,
    STATUS,
    LOGIN,
    PLAY;

    private val packets = Int2ObjectOpenHashMap<() -> Packet>()

    fun registerPacketType(id: Int, supplier: () -> Packet) {
        packets.putIfAbsent(id, supplier)
    }

    fun createPacket(id: Int) = packets[id]?.invoke()

    companion object {

        fun fromId(id: Int) = values()[id]
    }
}