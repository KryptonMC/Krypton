package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.extension.writeChat
import org.kryptonmc.krypton.extension.writeUUID
import org.kryptonmc.krypton.packet.state.PlayPacket
import java.util.*

class PacketOutChat(
    private val component: Component,
    private val position: ChatPosition,
    private val senderUUID: UUID
) : PlayPacket(0x0E) {

    override fun write(buf: ByteBuf) {
        buf.writeChat(component)
        buf.writeByte(position.id)
        buf.writeUUID(senderUUID)
    }
}

enum class ChatPosition(val id: Int) {

    CHAT_BOX(0),
    SYSTEM_MESSAGE(1),
    GAME_INFO(2)
}