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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.util.Positioning
import org.kryptonmc.krypton.util.writeAngle
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import java.util.UUID

@JvmRecord
data class PacketOutSpawnEntity(
    override val entityId: Int,
    val uuid: UUID,
    val typeId: Int,
    val x: Double,
    val y: Double,
    val z: Double,
    val pitch: Float,
    val yaw: Float,
    val headYaw: Float,
    val data: Int,
    val velocityX: Int,
    val velocityY: Int,
    val velocityZ: Int
) : EntityPacket {

    constructor(entity: KryptonEntity) : this(entity, 0F)

    constructor(entity: KryptonLivingEntity) : this(entity, entity.headYaw)

    private constructor(entity: KryptonEntity, headYaw: Float) : this(
        entity.id,
        entity.uuid,
        Registries.ENTITY_TYPE.idOf(entity.type),
        entity.location.x(),
        entity.location.y(),
        entity.location.z(),
        entity.rotation.y(),
        entity.rotation.x(),
        headYaw,
        0,
        Positioning.encodeVelocity(entity.velocity.x()),
        Positioning.encodeVelocity(entity.velocity.y()),
        Positioning.encodeVelocity(entity.velocity.z())
    )

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeUUID(uuid)
        buf.writeVarInt(typeId)
        buf.writeDouble(x)
        buf.writeDouble(y)
        buf.writeDouble(z)
        buf.writeAngle(yaw)
        buf.writeAngle(pitch)
        buf.writeAngle(headYaw)
        buf.writeInt(data)
        buf.writeShort(velocityX)
        buf.writeShort(velocityY)
        buf.writeShort(velocityZ)
    }
}
