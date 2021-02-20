package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.Abilities
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutAbilities(
    val abilities: Abilities
) : PlayPacket(0x30) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(abilities.flagsToProtocol())
        buf.writeFloat(abilities.flyingSpeed)
        buf.writeFloat(abilities.fieldOfViewModifier)
    }
}