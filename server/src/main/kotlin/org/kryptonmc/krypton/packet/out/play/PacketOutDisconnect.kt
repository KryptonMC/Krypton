package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sent to indicate that the client should close the connection to the server for the specified [reason].
 * This is different from [login disconnect][org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect],
 * which is sent in the [play][org.kryptonmc.krypton.packet.state.PacketState.PLAY] state.
 *
 * @param reason the reason for disconnection
 */
class PacketOutDisconnect(private val reason: Component) : PlayPacket(0x19) {

    override fun write(buf: ByteBuf) {
        buf.writeChat(reason)
    }
}
