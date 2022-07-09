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
import org.kryptonmc.krypton.util.decodeBlockX
import org.kryptonmc.krypton.util.decodeBlockY
import org.kryptonmc.krypton.util.decodeBlockZ
import org.kryptonmc.krypton.util.writeVector
import org.spongepowered.math.vector.Vector3i

@JvmRecord
data class PacketOutSetDefaultSpawnPosition(val x: Int, val y: Int, val z: Int, val angle: Float) : Packet {

    constructor(x: Int, y: Int, z: Int) : this(x, y, z, 0F)

    constructor(position: Vector3i, angle: Float) : this(position.x(), position.y(), position.z(), angle)

    constructor(position: Vector3i) : this(position, 0F)

    constructor(buf: ByteBuf) : this(buf.readLong(), buf.readFloat())

    private constructor(encoded: Long, angle: Float) : this(encoded.decodeBlockX(), encoded.decodeBlockY(), encoded.decodeBlockZ(), angle)

    override fun write(buf: ByteBuf) {
        buf.writeVector(x, y, z)
        buf.writeFloat(angle)
    }
}
