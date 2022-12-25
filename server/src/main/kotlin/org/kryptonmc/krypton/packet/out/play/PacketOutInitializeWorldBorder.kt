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
import org.kryptonmc.krypton.util.readVarLong
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVarLong
import org.kryptonmc.krypton.world.KryptonWorldBorder

@JvmRecord
data class PacketOutInitializeWorldBorder(
    val centerX: Double,
    val centerZ: Double,
    val oldSize: Double,
    val newSize: Double,
    val speed: Long,
    val teleportBoundary: Int,
    val warningBlocks: Int,
    val warningTime: Int
) : Packet {

    constructor(buf: ByteBuf) : this(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readVarLong(), buf.readVarInt(),
        buf.readVarInt(), buf.readVarInt())

    override fun write(buf: ByteBuf) {
        buf.writeDouble(centerX)
        buf.writeDouble(centerZ)
        buf.writeDouble(oldSize)
        buf.writeDouble(newSize)
        buf.writeVarLong(speed)
        buf.writeVarInt(teleportBoundary)
        buf.writeVarInt(warningBlocks)
        buf.writeVarInt(warningTime)
    }

    companion object {

        private const val PORTAL_TELEPORT_BOUNDARY = 29999984

        @JvmStatic
        fun create(border: KryptonWorldBorder): PacketOutInitializeWorldBorder {
            return PacketOutInitializeWorldBorder(border.centerX, border.centerZ, border.size, border.size, 0, PORTAL_TELEPORT_BOUNDARY,
                border.warningBlocks, border.warningTime)
        }
    }
}
