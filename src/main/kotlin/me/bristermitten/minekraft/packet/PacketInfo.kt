package me.bristermitten.minekraft.packet

import me.bristermitten.minekraft.packet.state.PacketState

interface PacketInfo
{
    val id: Int
    val state: PacketState
}
