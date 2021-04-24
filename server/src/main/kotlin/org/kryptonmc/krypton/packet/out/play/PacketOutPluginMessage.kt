package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeKey

/**
 * Designed as a way to allow mods and plugins to send data between client and server. Minecraft itself also
 * uses several plugin channels, of which we use the "minecraft:brand" channel.
 *
 * More information on plugin messaging can be found on Dinnerbone's blog
 * [here](https://dinnerbone.com/blog/2012/01/13/minecraft-plugin-channels-messaging/)
 *
 * @param channelName the name of the channel to send the message on
 * @param content the content of the plugin message
 */
class PacketOutPluginMessage(
    private val channelName: NamespacedKey,
    private val content: ByteArray
) : PlayPacket(0x17) {

    override fun write(buf: ByteBuf) {
        buf.writeKey(channelName)
        buf.writeBytes(content)
    }
}
