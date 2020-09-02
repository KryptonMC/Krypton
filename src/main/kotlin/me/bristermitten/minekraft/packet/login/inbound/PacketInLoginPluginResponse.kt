package me.bristermitten.minekraft.packet.login.inbound

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.readAllAvailableBytes
import me.bristermitten.minekraft.packet.login.LoginPluginPacket
import me.bristermitten.minekraft.packet.login.PluginPacketType
import kotlin.properties.Delegates

class PacketInLoginPluginResponse : LoginPluginPacket(PluginPacketType.RESPONSE) {

    override var messageID by Delegates.notNull<Int>()

    lateinit var responseStatus: PluginResponseStatus

    override lateinit var data: Array<Byte>

    override fun read(buf: ByteBuf) {
        messageID = buf.readInt()
        responseStatus = if (buf.readBoolean()) PluginResponseStatus.SUCCESS else PluginResponseStatus.FAILURE
        data = buf.readAllAvailableBytes().toTypedArray()
    }
}

enum class PluginResponseStatus {

    SUCCESS,
    FAILURE
}