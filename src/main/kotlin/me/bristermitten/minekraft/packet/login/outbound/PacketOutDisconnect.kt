package me.bristermitten.minekraft.packet.login.outbound

import io.netty.buffer.ByteBuf
import kotlinx.serialization.json.JsonObject
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.packet.login.LoginPacket

class PacketOutDisconnect(
    val reason: JsonObject
) : LoginPacket(0x00) {

    override fun write(buf: ByteBuf) {
        buf.writeString(reason.toString()) // reason as JSON object (chat format)
    }
}