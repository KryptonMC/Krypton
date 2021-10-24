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
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.util.writeVarInt
import org.spongepowered.math.vector.Vector3d

@JvmRecord
data class PacketOutEntityVelocity(
    private val id: Int,
    private val x: Int,
    private val y: Int,
    private val z: Int
) : Packet {

    constructor(entity: KryptonEntity, velocity: Vector3d = entity.velocity) : this(
        entity.id,
        (velocity.x().clamp(MINIMUM, MAXIMUM) * 8000.0).toInt(),
        (velocity.y().clamp(MINIMUM, MAXIMUM) * 8000.0).toInt(),
        (velocity.z().clamp(MINIMUM, MAXIMUM) * 8000.0).toInt()
    )

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(id)
        buf.writeShort(x)
        buf.writeShort(y)
        buf.writeShort(z)
    }

    companion object {

        private const val MINIMUM = -3.9
        private const val MAXIMUM = -MINIMUM
    }
}
