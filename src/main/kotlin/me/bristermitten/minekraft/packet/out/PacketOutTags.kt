package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.play.PlayPacket

class PacketOutTags : PlayPacket(0x5C) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(0)
        buf.writeVarInt(0)
        buf.writeVarInt(0)
        buf.writeVarInt(0)
//        buf.writeTagArray("minecraft:stone")
//        buf.writeTagArray("minecraft:stone")
//        buf.writeTagArray()
//        buf.writeTagArray("minecraft:player")
    }

    private fun ByteBuf.writeTagArray(vararg entries: String) {

        writeVarInt(entries.size)
        for (entry in entries) {
            writeString(entry)
            writeVarInt(0)
        }
    }
}
