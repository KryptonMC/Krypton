package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.bardy.komponent.Component
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeUUID
import org.kryptonmc.krypton.packet.state.PlayPacket
import java.util.*

class PacketOutChat(
    private val component: Component,
    private val position: ChatPosition,
    private val senderUUID: UUID
) : PlayPacket(0x0E) {

    override fun write(buf: ByteBuf) {
        buf.writeString(JSON.encodeToString(component))
        buf.writeByte(position.id)
        buf.writeUUID(senderUUID)
    }

    companion object {

        private val JSON = Json {}
    }
}

enum class ChatPosition(val id: Int) {

    CHAT_BOX(0),
    SYSTEM_MESSAGE(1),
    GAME_INFO(2)
}