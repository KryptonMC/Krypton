package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.entity.Abilities
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutAbilities(private val abilities: Abilities) : PlayPacket(0x30) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(abilities.flagsToProtocol())
        buf.writeFloat(abilities.flyingSpeed)
        buf.writeFloat(abilities.fieldOfViewModifier)
    }
}

private fun Abilities.flagsToProtocol(): Int {
    var flagsByte = 0x0
    if (isInvulnerable) flagsByte += 0x01
    if (canFly) flagsByte += 0x02
    if (isFlyingAllowed) flagsByte += 0x04
    if (isCreativeMode) flagsByte += 0x08
    return flagsByte
}