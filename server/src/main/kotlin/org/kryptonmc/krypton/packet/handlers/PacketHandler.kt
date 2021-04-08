package org.kryptonmc.krypton.packet.handlers

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.session.Session

interface PacketHandler {

    val server: KryptonServer

    val session: Session

    fun handle(packet: Packet)
}