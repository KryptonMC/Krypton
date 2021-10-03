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
import org.kryptonmc.krypton.entity.hanging.KryptonPainting
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.data2D
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVector

@JvmRecord
data class PacketOutSpawnPainting(private val painting: KryptonPainting) : Packet {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(painting.id)
        buf.writeUUID(painting.uuid)
        val canvas = painting.canvas
        buf.writeVarInt(if (canvas != null) InternalRegistries.CANVAS.idOf(canvas) else DEFAULT_CANVAS_ID)
        buf.writeVector(painting.centerPosition)
        buf.writeByte(painting.direction.data2D())
    }

    companion object {

        private val DEFAULT_CANVAS_ID = InternalRegistries.CANVAS.idOf(InternalRegistries.CANVAS.defaultValue)
    }
}
