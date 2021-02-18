package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import kotlinx.serialization.json.Json
import me.bardy.komponent.Component
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.extension.writeUUID
import me.bristermitten.minekraft.packet.state.PlayPacket
import java.util.*

class PacketOutChat(
    private val component: Component,
    private val position: ChatPosition,
    private val senderUUID: UUID
) : PlayPacket(0x0E) {

    override fun write(buf: ByteBuf) {
        buf.writeString(JSON.encodeToString(Component.Companion, component))
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