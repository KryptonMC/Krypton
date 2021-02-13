package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.extension.writeUUID
import me.bristermitten.minekraft.packet.state.PlayPacket
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import java.util.*

class PacketOutChat(
    private val component: Component,
    private val position: ChatPosition,
    private val senderUUID: UUID
) : PlayPacket(0x0E) {

    override fun write(buf: ByteBuf) {
        buf.writeString(GsonComponentSerializer.gson().serialize(component))
        buf.writeByte(position.id)
        buf.writeUUID(senderUUID)
    }
}

enum class ChatPosition(val id: Int) {

    CHAT_BOX(0),
    SYSTEM_MESSAGE(1),
    GAME_INFO(2)
}