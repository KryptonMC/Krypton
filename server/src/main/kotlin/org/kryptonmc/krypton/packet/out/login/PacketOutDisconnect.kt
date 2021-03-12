package org.kryptonmc.krypton.packet.out.login

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.extension.writeChat
import org.kryptonmc.krypton.packet.state.LoginPacket

class PacketOutDisconnect(private val component: Component) : LoginPacket(0x00) {

    override fun write(buf: ByteBuf) {
        buf.writeChat(component)
    }
}