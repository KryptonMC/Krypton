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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.hanging.KryptonPainting
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.util.data2D
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVector
import java.util.UUID

@JvmRecord
data class PacketOutSpawnPainting(
    override val entityId: Int,
    val uuid: UUID,
    val pictureId: Int,
    val centerX: Int,
    val centerY: Int,
    val centerZ: Int,
    val directionData: Int
) : EntityPacket {

    constructor(painting: KryptonPainting) : this(
        painting.id,
        painting.uuid,
        if (painting.picture != null) Registries.PICTURES.idOf(painting.picture!!) else DEFAULT_CANVAS_ID,
        painting.centerPosition.x(),
        painting.centerPosition.y(),
        painting.centerPosition.z(),
        painting.direction.data2D()
    )

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeUUID(uuid)
        buf.writeVarInt(pictureId)
        buf.writeVector(centerX, centerY, centerZ)
        buf.writeByte(directionData)
    }

    companion object {

        private val DEFAULT_CANVAS_ID = Registries.PICTURES.idOf(Registries.PICTURES.defaultValue)
    }
}
