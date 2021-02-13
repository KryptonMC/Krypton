package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutPluginMessage : PlayPacket(0x17)
{
    override fun write(buf: ByteBuf)
    {
        buf.writeString("minecraft:brand") // brand channel
        buf.writeVarInt(9) // length of brand string
        buf.writeString("MineKraft") // brand string
    }
}
