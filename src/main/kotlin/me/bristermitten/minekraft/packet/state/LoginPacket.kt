package me.bristermitten.minekraft.packet.state

import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo

open class LoginPacket(private val id: Int) : Packet
{
    override val info = object : PacketInfo
    {
        override val id = this@LoginPacket.id
        override val state = PacketState.LOGIN
    }
}
