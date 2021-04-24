package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.registry.toNamespacedKey
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.readAllAvailableBytes
import org.kryptonmc.krypton.util.readString

/**
 * Sent when the client sends a plugin message on the specified [channel]. Currently only triggers
 * the [plugin message event][org.kryptonmc.krypton.api.event.events.play.PluginMessageEvent].
 */
class PacketInPluginMessage : PlayPacket(0x0B) {

    /**
     * The channel the plugin message was sent on
     */
    lateinit var channel: NamespacedKey private set

    /**
     * The raw data sent on the channel. Could be a string, could be a number, could just
     * be raw bytes, we don't know, it's context-related.
     */
    lateinit var data: ByteArray private set

    override fun read(buf: ByteBuf) {
        channel = buf.readString().toNamespacedKey()
        data = buf.readAllAvailableBytes()
    }
}
