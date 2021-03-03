package org.kryptonmc.krypton.packet.out.login

import io.netty.buffer.ByteBuf
import kotlinx.serialization.json.Json
import me.bardy.komponent.Component
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.packet.state.LoginPacket

class PacketOutDisconnect(private val component: Component) : LoginPacket(0x00) {

    override fun write(buf: ByteBuf) {
        buf.writeString(JSON.encodeToString(Component.Companion, component))
    }

    companion object {

        private val JSON = Json {}
    }
}