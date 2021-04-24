package org.kryptonmc.krypton.packet

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PacketState

/**
 * Super interface for all inbound and outbound packets.
 */
interface Packet {

    /**
     * This packet's info
     */
    val info: PacketInfo

    /**
     * Read this packet's data from the given [buf]
     */
    fun read(buf: ByteBuf) {
        throw UnsupportedOperationException("$javaClass does not support reading")
    }

    /**
     * Write this packet's data to the given [buf]
     */
    fun write(buf: ByteBuf) {
        throw UnsupportedOperationException("$javaClass does not support writing")
    }
}

/**
 * Holder for packet information
 */
data class PacketInfo(val id: Int, val state: PacketState)
