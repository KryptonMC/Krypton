/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.krypton.entity.player.Abilities
import org.kryptonmc.krypton.packet.Packet

@JvmRecord
data class PacketOutAbilities(
    val isInvulnerable: Boolean,
    val isFlying: Boolean,
    val canFly: Boolean,
    val canInstantlyBuild: Boolean,
    val flyingSpeed: Float,
    val walkingSpeed: Float
) : Packet {

    constructor(abilities: Abilities) : this(
        abilities.invulnerable,
        abilities.flying,
        abilities.canFly,
        abilities.canInstantlyBuild,
        abilities.flyingSpeed,
        abilities.walkingSpeed
    )

    constructor(buf: ByteBuf) : this(buf.readByte().toInt(), buf.readFloat(), buf.readFloat())

    private constructor(flags: Int, flyingSpeed: Float, walkingSpeed: Float) : this(
        flags and FLAG_INVULNERABLE != 0,
        flags and FLAG_FLYING != 0,
        flags and FLAG_CAN_FLY != 0,
        flags and FLAG_CAN_INSTANTLY_BUILD != 0,
        flyingSpeed,
        walkingSpeed
    )

    override fun write(buf: ByteBuf) {
        var flags = 0
        if (isInvulnerable) flags = flags or FLAG_INVULNERABLE
        if (isFlying) flags = flags or FLAG_FLYING
        if (canFly) flags = flags or FLAG_CAN_FLY
        if (canInstantlyBuild) flags = flags or FLAG_CAN_INSTANTLY_BUILD
        buf.writeByte(flags)
        buf.writeFloat(flyingSpeed)
        buf.writeFloat(walkingSpeed)
    }

    companion object {

        private const val FLAG_INVULNERABLE = 0x01
        private const val FLAG_FLYING = 0x02
        private const val FLAG_CAN_FLY = 0x04
        private const val FLAG_CAN_INSTANTLY_BUILD = 0x08
    }
}
