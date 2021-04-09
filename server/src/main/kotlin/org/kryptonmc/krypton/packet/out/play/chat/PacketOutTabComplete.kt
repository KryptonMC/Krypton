package org.kryptonmc.krypton.packet.out.play.chat

import com.mojang.brigadier.Message
import com.mojang.brigadier.suggestion.Suggestions
import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.extension.writeChat
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutTabComplete(
    private val id: Int,
    private val matches: Suggestions
) : PlayPacket(0x0F) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(id)
        buf.writeVarInt(matches.range.start)
        buf.writeVarInt(matches.range.length)

        buf.writeVarInt(matches.list.size)
        matches.list.forEach {
            buf.writeString(it.text)
            buf.writeBoolean(it.tooltip != null)
            if (it.tooltip != null) buf.writeChat(it.tooltip.toAdventure())
        }
    }
}

private fun Message.toAdventure() = Component.text(string)