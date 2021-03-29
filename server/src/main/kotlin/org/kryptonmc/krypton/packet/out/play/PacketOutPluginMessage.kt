package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeKey
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.api.registry.NamespacedKey

class PacketOutPluginMessage(
    private val channelName: NamespacedKey,
    private val content: ByteArray
) : PlayPacket(0x17) {

    override fun write(buf: ByteBuf) {
        buf.writeKey(channelName)
        buf.writeBytes(content)
    }
}