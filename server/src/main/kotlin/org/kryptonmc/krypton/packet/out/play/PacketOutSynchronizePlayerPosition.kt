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
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeVarInt
import org.spongepowered.math.vector.Vector2f
import org.spongepowered.math.vector.Vector3d
import kotlin.random.Random

@JvmRecord
data class PacketOutSynchronizePlayerPosition(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
    val flags: Int = 0,
    val teleportId: Int = Random.nextInt(1000),
    val shouldDismount: Boolean = false
) : Packet {

    constructor(
        position: Vector3d,
        rotation: Vector2f,
        flags: Int = 0,
        teleportId: Int = Random.nextInt(1000),
        shouldDismount: Boolean = false
    ) : this(position.x(), position.y(), position.z(), rotation.x(), rotation.y(), flags, teleportId, shouldDismount)

    constructor(buf: ByteBuf) : this(
        buf.readDouble(),
        buf.readDouble(),
        buf.readDouble(),
        buf.readFloat(),
        buf.readFloat(),
        buf.readByte().toInt(),
        buf.readVarInt(),
        buf.readBoolean()
    )

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
}