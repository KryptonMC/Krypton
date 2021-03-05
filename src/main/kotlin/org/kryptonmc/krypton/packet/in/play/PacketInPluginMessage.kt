package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.readString
import org.kryptonmc.krypton.extension.readVarIntByteArray
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.registry.NamespacedKey
import org.kryptonmc.krypton.registry.toNamespacedKey

class PacketInPluginMessage : PlayPacket(0x0B) {

    lateinit var channel: NamespacedKey
        private set

    lateinit var data: ByteArray
        private set

    override fun read(buf: ByteBuf) {
        channel = buf.readString().toNamespacedKey()
        data = buf.readVarIntByteArray()
    }
}