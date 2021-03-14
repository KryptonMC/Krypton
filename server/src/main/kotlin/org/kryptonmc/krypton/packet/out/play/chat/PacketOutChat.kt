package org.kryptonmc.krypton.packet.out.play.chat

import io.netty.buffer.ByteBuf
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.extension.writeChat
import org.kryptonmc.krypton.extension.writeUUID
import org.kryptonmc.krypton.packet.state.PlayPacket
import java.util.*

class PacketOutChat(
    private val component: Component,
    private val type: MessageType,
    private val senderUUID: UUID
) : PlayPacket(0x0E) {

    override fun write(buf: ByteBuf) {
        buf.writeChat(component)
        buf.writeByte(type.id)
        buf.writeUUID(senderUUID)
    }

    private val MessageType.id: Int
        get() = when (this) {
            MessageType.CHAT -> 0
            MessageType.SYSTEM -> 1
        }
}