package org.kryptonmc.krypton.packet.state

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.kryptonmc.krypton.packet.Packet

/**
 * Represents one of the four packet states in the Minecraft protocol. Each one of these states has
 * different packets and different purposes.
 *
 * @author Alex Wood
 */
enum class PacketState {

    /**
     * In the handshake state, the client informs the server of their intentions
     */
    HANDSHAKE,

    /**
     * In the status state, the client requests information about the server, such as the current player
     * count, max player count, ping and MOTD.
     */
    STATUS,

    /**
     * In the login state, the client attempts to login to the server, and this state establishes an
     * encrypted connection with the client (when the server is in online mode). The server will also
     * set a compression threshold for packet compression, if the threshold set in the config is > 0
     */
    LOGIN,

    /**
     * The vast majority of packets that exist in the protocol are in the play state. This state
     * indicates that the client is playing the game.
     */
    PLAY;

    private val packets = Int2ObjectOpenHashMap<() -> Packet>()

    /**
     * Register a packet type with the specified [id]
     *
     * @param id the ID of the packet
     * @param supplier a function that can be used to create instances of the packet.
     */
    fun registerPacketType(id: Int, supplier: () -> Packet) {
        packets.putIfAbsent(id, supplier)
    }

    /**
     * Create a new packet from the specified [id]
     *
     * @param id the packet ID
     * @return a new packet with the specified [id], or null if there is no packet
     * registered with this ID
     */
    fun createPacket(id: Int) = packets[id]?.invoke()

    companion object {

        /**
         * Get a packet state from its protocol ID
         */
        fun fromId(id: Int) = values()[id]
    }
}