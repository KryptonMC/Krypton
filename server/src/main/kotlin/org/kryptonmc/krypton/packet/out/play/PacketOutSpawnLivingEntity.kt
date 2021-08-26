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
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.writeAngle
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutSpawnLivingEntity(private val entity: KryptonLivingEntity) : Packet {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entity.id)
        buf.writeUUID(entity.uuid)
        buf.writeVarInt(InternalRegistries.ENTITY_TYPE.idOf(entity.type))
        buf.writeDouble(entity.location.x)
        buf.writeDouble(entity.location.y)
        buf.writeDouble(entity.location.z)
        buf.writeAngle(entity.location.yaw)
        buf.writeAngle(entity.location.pitch)
        buf.writeByte(0)
        buf.writeShort(entity.velocity.x.toInt())
        buf.writeShort(entity.velocity.y.toInt())
        buf.writeShort(entity.velocity.z.toInt())
    }
}
