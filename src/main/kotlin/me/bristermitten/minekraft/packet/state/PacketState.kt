package me.bristermitten.minekraft.packet.state

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import me.bristermitten.minekraft.packet.Packet

enum class PacketState(val id: Int) {

    HANDSHAKE(0),
    STATUS(1),
    LOGIN(2),
    PLAY(3);

    private val packets: Int2ObjectMap<() -> Packet> = Int2ObjectArrayMap()

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