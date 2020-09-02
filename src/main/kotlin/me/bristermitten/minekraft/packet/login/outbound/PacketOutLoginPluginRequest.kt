package me.bristermitten.minekraft.packet.login.outbound

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelId
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.packet.login.LoginPluginPacket
import me.bristermitten.minekraft.packet.login.PluginPacketType
import kotlin.random.Random

class PacketOutLoginPluginRequest(
    val channelId: ChannelId,
    override val data: Array<Byte>
) : LoginPluginPacket(PluginPacketType.REQUEST) {

    override val messageID = Random.Default.nextInt()

    override fun write(buf: ByteBuf) {
        buf.writeInt(messageID)
        buf.writeString(channelId.asLongText())
        buf.writeBytes(data.toByteArray())
    }
}