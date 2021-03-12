package org.kryptonmc.krypton.packet.out.play.chat

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.extension.writeChat
import org.kryptonmc.krypton.packet.state.PlayPacket

// Unused by the server, for future use in plugins
class PacketOutPlayerListHeaderFooter(
    private val header: Component,
    private val footer: Component
) : PlayPacket(0x53) {

    override fun write(buf: ByteBuf) {
        buf.writeChat(header)
        buf.writeChat(footer)
    }
}