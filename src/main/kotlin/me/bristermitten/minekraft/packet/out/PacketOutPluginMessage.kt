package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.packet.state.PlayPacket
import me.bristermitten.minekraft.registry.NamespacedKey

class PacketOutPluginMessage(
    val channelName: NamespacedKey,
    val content: String
) : PlayPacket(0x17) {

    override fun write(buf: ByteBuf) {
        buf.writeString(channelName.toString())
        buf.writeString(content)
    }
}