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
import org.kryptonmc.krypton.coordinate.Positioning
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.packet.MovementPacket
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutUpdateEntityPositionAndRotation(
    override val entityId: Int,
    val deltaX: Short,
    val deltaY: Short,
    val deltaZ: Short,
    val yaw: Byte,
    val pitch: Byte,
    override val onGround: Boolean
) : EntityPacket, MovementPacket {

    constructor(entityId: Int, dx: Short, dy: Short, dz: Short, yaw: Float, pitch: Float,
                onGround: Boolean) : this(entityId, dx, dy, dz, Positioning.encodeRotation(yaw), Positioning.encodeRotation(pitch), onGround)

    constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readShort(), buf.readShort(), buf.readShort(), buf.readByte(), buf.readByte(),
        buf.readBoolean())

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeShort(deltaX.toInt())
        buf.writeShort(deltaY.toInt())
        buf.writeShort(deltaZ.toInt())
        buf.writeByte(yaw.toInt())
        buf.writeByte(pitch.toInt())
        buf.writeBoolean(onGround)
    }
}
