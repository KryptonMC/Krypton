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
import org.kryptonmc.api.map.decoration.MapDecoration
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVarIntByteArray

@JvmRecord
data class PacketOutMapData(
    val id: Int,
    val scale: Int,
    val locked: Boolean,
    val decorations: Collection<MapDecoration>?,
    val colorUpdate: MapColorUpdate?
) : Packet {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(id)
        buf.writeByte(scale)
        buf.writeBoolean(locked)
        if (decorations != null) buf.writeCollection(decorations) {
            buf.writeEnum(it.type)
            buf.writeByte(it.x)
            buf.writeByte(it.y)
            buf.writeByte(it.orientation.ordinal)
            buf.writeBoolean(it.name != null)
            if (it.name != null) buf.writeChat(it.name!!)
        }
        if (colorUpdate != null) colorUpdate.write(buf) else buf.writeByte(0)
    }

    @Suppress("ArrayInDataClass")
    @JvmRecord
    data class MapColorUpdate(val startX: Int, val startZ: Int, val width: Int, val height: Int, val colors: ByteArray) : Writable {

        override fun write(buf: ByteBuf) {
            buf.writeByte(width)
            buf.writeByte(height)
            buf.writeByte(startX)
            buf.writeByte(startZ)
            buf.writeVarIntByteArray(colors)
        }
    }
}
