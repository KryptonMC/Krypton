package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutPluginMessage : PlayPacket(0x19)
{
    override fun write(buf: ByteBuf)
    {
        buf.writeString("brand")
        buf.writeString("MineKraft")
    }
}
