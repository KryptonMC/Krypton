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
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutSynchronizePlayerPosition(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
    val flags: Int = 0,
    val teleportId: Int = RANDOM.nextInt(RANDOM_TELEPORT_ID_UPPER_BOUND),
    val shouldDismount: Boolean = false
) : Packet {

    constructor(player: KryptonPlayer) : this(player.location.x, player.location.y, player.location.z, player.yaw, player.pitch)

    constructor(buf: ByteBuf) : this(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), buf.readFloat(), buf.readByte().toInt(),
        buf.readVarInt(), buf.readBoolean())

    override fun write(buf: ByteBuf) {
        buf.writeDouble(x)
        buf.writeDouble(y)
        buf.writeDouble(z)
        buf.writeFloat(yaw)
        buf.writeFloat(pitch)
        buf.writeByte(flags)
        buf.writeVarInt(teleportId)
        buf.writeBoolean(shouldDismount)
    }

    companion object {

        private val RANDOM = RandomSource.createThreadSafe()
        private const val RANDOM_TELEPORT_ID_UPPER_BOUND = 1000 // A number I chose because it seems sensible enough
    }
}
