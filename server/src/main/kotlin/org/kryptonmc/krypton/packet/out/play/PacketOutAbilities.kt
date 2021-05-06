/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.entity.Abilities
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sets the current abilities for the client. Note that this is not incremental (all values are updated to
 * the values present in the new [abilities])
 *
 * @param abilities the abilities to set
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
