package org.kryptonmc.krypton.packet.out.play.chat

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeChat

/**
 * This only exists in the protocol for use in modded servers. It is never used by the official vanilla
 * server. How nice of Mojang to do that for us :)
 *
 * Informs the client of the component to display above ([header] of the list) or below ([footer] of the list)
 * the player list.
 */
class PacketOutPlayerListHeaderFooter(
    private val header: Component,
    private val footer: Component
) : PlayPacket(0x53) {

    override fun write(buf: ByteBuf) {
        buf.writeChat(header)
        buf.writeChat(footer)
    }
}
