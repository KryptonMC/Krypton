package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.extension.writeChat
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutDisconnect(private val component: Component) : PlayPacket(0x19) {

    override fun write(buf: ByteBuf) {
        buf.writeChat(component)
    }
}