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
        buf.writeInt(1)
        buf.writeByte(0)
        buf.writeInt(0)
        buf.writeLong(12345678L)
        buf.writeByte(5)
        buf.writeString("default")
        buf.writeVarInt(16)
        buf.writeBoolean(false)
        buf.writeBoolean(false)
    }

}
