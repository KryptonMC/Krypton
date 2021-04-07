package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.registry.toNamespacedKey
import org.kryptonmc.krypton.extension.readAllAvailableBytes
import org.kryptonmc.krypton.extension.readString
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketInPluginMessage : PlayPacket(0x0B) {

    lateinit var channel: NamespacedKey
        private set

    lateinit var data: ByteArray
        private set

    override fun read(buf: ByteBuf) {
        channel = buf.readString().toNamespacedKey()
        data = buf.readAllAvailableBytes()
    }
}