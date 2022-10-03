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
import org.kryptonmc.krypton.world.WorldEvent
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.Positioning
import org.kryptonmc.krypton.util.writeVector
import org.spongepowered.math.vector.Vector3i

@JvmRecord
data class PacketOutWorldEvent(val event: WorldEvent, val x: Int, val y: Int, val z: Int, val data: Int, val isGlobal: Boolean) : Packet {

    constructor(
        event: WorldEvent,
        position: Vector3i,
        data: Int,
        isGlobal: Boolean
    ) : this(event, position.x(), position.y(), position.z(), data, isGlobal)

    constructor(buf: ByteBuf) : this(WorldEvent.fromId(buf.readInt())!!, buf.readLong(), buf.readInt(), buf.readBoolean())

    private constructor(
        event: WorldEvent,
        encoded: Long,
        data: Int,
        isGlobal: Boolean
    ) : this(event, Positioning.decodeBlockX(encoded), Positioning.decodeBlockY(encoded), Positioning.decodeBlockZ(encoded), data, isGlobal)

    override fun write(buf: ByteBuf) {
        buf.writeInt(event.id)
        buf.writeVector(x, y, z)
        buf.writeInt(data)
        buf.writeBoolean(isGlobal)
    }
}
