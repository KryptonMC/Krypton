package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import kotlinx.serialization.json.Json
import me.bardy.komponent.Component
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.packet.state.LoginPacket

class PacketOutDisconnect(
    private val component: Component
) : LoginPacket(0x00) {

    override fun write(buf: ByteBuf) {
        buf.writeString(JSON.encodeToString(Component.Companion, component))
    }

    companion object {

        private val JSON = Json {}
    }
}