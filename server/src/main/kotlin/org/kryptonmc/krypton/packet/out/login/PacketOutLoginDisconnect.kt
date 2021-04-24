package org.kryptonmc.krypton.packet.out.login

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.packet.state.LoginPacket
import org.kryptonmc.krypton.util.writeChat

/**
 * Informs the client that they have been disconnected for the specified [reason]
 * The client assumes that the connection has already been dropped by this point.
 */
class PacketOutLoginDisconnect(private val reason: Component) : LoginPacket(0x00) {

    override fun write(buf: ByteBuf) {
        buf.writeChat(reason)
    }
}
