package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketInfo
import me.bristermitten.minekraft.packet.state.PacketState

class PacketOutJoinGame : Packet
{
    override val info = Companion

    companion object : PacketInfo
    {
        override val id = 0x26
        override val state = PacketState.PLAY
    }

    override fun write(buf: ByteBuf)
    {
        buf.writeInt(1) //EID
        buf.writeByte(0) //gamemode
        buf.writeInt(0) //dimension


        buf.writeLong(11111111) //Hashed Seed
        buf.writeByte(0) //Max Players
        buf.writeString("default") //Level Type
        buf.writeVarInt(16) //Render Distance
        buf.writeBoolean(false) //Reduced Debug Info
        buf.writeBoolean(false) //Respawn Screen

    }

}
