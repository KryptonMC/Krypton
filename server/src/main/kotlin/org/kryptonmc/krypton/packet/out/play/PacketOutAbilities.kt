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
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet

class PacketOutAbilities(
    private val isInvulnerable: Boolean,
    private val isFlying: Boolean,
    private val canFly: Boolean,
    private val canInstantlyBuild: Boolean,
    private val flyingSpeed: Float,
    private val walkingSpeed: Float
) : Packet {

    constructor(player: KryptonPlayer) : this(player.isInvulnerable, player.isFlying, player.canFly, player.canInstantlyBuild, player.flyingSpeed, player.walkingSpeed)

    override fun write(buf: ByteBuf) {
        var flags = 0
        if (isInvulnerable) flags = flags or 1
        if (isFlying) flags = flags or 2
        if (canFly) flags = flags or 4
        if (canInstantlyBuild) flags = flags or 8
        buf.writeByte(flags)
        buf.writeFloat(flyingSpeed)
        buf.writeFloat(walkingSpeed)
    }
}
