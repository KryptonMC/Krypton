package org.kryptonmc.krypton.packet.handlers

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.session.Session

/**
 * The base interface for packet handlers. This exists primarily to abstract away the [handle]
 * function, so we can call it without actually knowing which handler will handle it. :wesmart:
 *
 * @author Callum Seabrook
 */
interface PacketHandler {

    /**
     * The server that this handler is running on
     */
    val server: KryptonServer

    /**
     * The session that this handler handles packets for
     */
    val session: Session

    /**
     * Handle the specified [packet]
     *
     * @param packet the packet to handle
     */
    fun handle(packet: Packet)
}