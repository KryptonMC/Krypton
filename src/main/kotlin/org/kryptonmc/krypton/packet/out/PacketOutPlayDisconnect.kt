package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.bardy.komponent.Component
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutPlayDisconnect(
    val component: Component
) : PlayPacket(0x19) {

    override fun write(buf: ByteBuf) {
        buf.writeString(JSON.encodeToString(component))
    }

    companion object {

        private val JSON = Json {}
    }
}