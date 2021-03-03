package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.registry.NamespacedKey

class PacketOutPluginMessage(
    private val channelName: NamespacedKey,
    private val content: String
) : PlayPacket(0x17) {

    override fun write(buf: ByteBuf) {
        buf.writeString(channelName.toString())
        buf.writeString(content)
    }
}