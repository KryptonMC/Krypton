package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.entity.Abilities
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sets the current abilities for the client. Note that this is not incremental (all values are updated to
 * the values present in the new [abilities])
 *
 * @param abilities the abilities to set
 *
 * @author Callum Seabrook
 */
class PacketOutAbilities(private val abilities: Abilities) : PlayPacket(0x30) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(abilities.flagsToProtocol())
        buf.writeFloat(abilities.flyingSpeed)
        buf.writeFloat(abilities.walkSpeed)
    }
}

private fun Abilities.flagsToProtocol(): Int {
    var flagsByte = 0x0
    if (isInvulnerable) flagsByte += 0x01
    if (isFlying) flagsByte += 0x02
    if (canFly) flagsByte += 0x04
    if (canInstantlyBuild) flagsByte += 0x08
    return flagsByte
}