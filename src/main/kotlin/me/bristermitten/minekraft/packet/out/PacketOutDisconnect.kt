package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.packet.state.LoginPacket
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

class PacketOutDisconnect(
    private val component: Component
) : LoginPacket(0x00) {

    override fun write(buf: ByteBuf) {
        buf.writeString(GsonComponentSerializer.gson().serialize(component))
    }
}