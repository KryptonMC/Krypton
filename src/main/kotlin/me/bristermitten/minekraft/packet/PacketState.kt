package me.bristermitten.minekraft.packet

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import it.unimi.dsi.fastutil.ints.Int2ObjectMap

enum class PacketState(private val id: Int)
{
    HANDSHAKE(0),
    STATUS(1),
    LOGIN(2),
    PLAY(3);

    private val packets: Int2ObjectMap<() -> Packet> = Int2ObjectArrayMap()


    fun registerPacketType(id: Int, supplier: () -> Packet)
    {
        packets.putIfAbsent(id, supplier)
    }

    fun createPacket(id: Int): Packet?
    {
        val packetCreator = packets[id]
        return packetCreator?.invoke()
    }

    companion object
    {
        private val values = values()
        fun fromId(id: Int): PacketState
        {
            return values[id]
        }
    }
}
