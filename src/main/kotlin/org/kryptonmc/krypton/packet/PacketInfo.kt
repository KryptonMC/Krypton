package org.kryptonmc.krypton.packet

import org.kryptonmc.krypton.packet.state.PacketState

interface PacketInfo {

    val id: Int
    val state: PacketState
}