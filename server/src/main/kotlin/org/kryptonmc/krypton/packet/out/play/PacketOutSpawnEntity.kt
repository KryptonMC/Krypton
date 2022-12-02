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
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.Positioning
import org.kryptonmc.krypton.util.readAngle
import org.kryptonmc.krypton.util.readById
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeAngle
import org.kryptonmc.krypton.util.writeId
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import java.util.UUID

@JvmRecord
data class PacketOutSpawnEntity(
    override val entityId: Int,
    val uuid: UUID,
    val type: Type,
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

    private constructor(entity: KryptonEntity, headYaw: Float) : this(entity.id, entity.uuid, entity.type, entity.position.x, entity.position.y,
        entity.position.z, entity.pitch, entity.yaw, headYaw, 0, entity.velocityX(), entity.velocityY(), entity.velocityZ())

    constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readUUID(), buf.readById(KryptonRegistries.ENTITY_TYPE)!!, buf.readDouble(),
        buf.readDouble(), buf.readDouble(), buf.readAngle(), buf.readAngle(), buf.readAngle(), buf.readInt(), buf.readShort().toInt(),
        buf.readShort().toInt(), buf.readShort().toInt())

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeUUID(uuid)
        buf.writeId(KryptonRegistries.ENTITY_TYPE, type)
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

@Suppress("NOTHING_TO_INLINE")
private inline fun KryptonEntity.velocityX(): Int = Positioning.encodeVelocity(velocity.x)

@Suppress("NOTHING_TO_INLINE")
private inline fun KryptonEntity.velocityY(): Int = Positioning.encodeVelocity(velocity.y)

@Suppress("NOTHING_TO_INLINE")
private inline fun KryptonEntity.velocityZ(): Int = Positioning.encodeVelocity(velocity.z)

// This purely exists to avoid annoying wrapping due to the 150 char limit
private typealias Type = KryptonEntityType<*>
