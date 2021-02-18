package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entity.Abilities
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutAbilities(
    val abilities: Abilities
) : PlayPacket(0x30) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(abilities.flagsToProtocol())
        buf.writeFloat(abilities.flyingSpeed)
        buf.writeFloat(abilities.fieldOfViewModifier)
    }
}