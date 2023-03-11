/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.util.Position
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.coordinate.Positioning
import org.kryptonmc.krypton.util.readById
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeId
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import java.util.UUID

@JvmRecord
data class PacketOutSpawnEntity(
    override val entityId: Int,
    val uuid: UUID,
    val type: KryptonEntityType<*>,
    val x: Double,
    val y: Double,
    val z: Double,
    val pitch: Byte,
    val yaw: Byte,
    val headYaw: Byte,
    val data: Int,
    val velocityX: Int,
    val velocityY: Int,
    val velocityZ: Int
) : EntityPacket {

    constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readUUID(), buf.readById(KryptonRegistries.ENTITY_TYPE)!!, buf.readDouble(),
        buf.readDouble(), buf.readDouble(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readVarInt(), buf.readShort().toInt(),
        buf.readShort().toInt(), buf.readShort().toInt())

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeUUID(uuid)
        buf.writeId(KryptonRegistries.ENTITY_TYPE, type)
        buf.writeDouble(x)
        buf.writeDouble(y)
        buf.writeDouble(z)
        buf.writeByte(pitch.toInt())
        buf.writeByte(yaw.toInt())
        buf.writeByte(headYaw.toInt())
        buf.writeVarInt(data)
        buf.writeShort(velocityX)
        buf.writeShort(velocityY)
        buf.writeShort(velocityZ)
    }

    companion object {

        @JvmStatic
        fun create(entity: KryptonEntity): PacketOutSpawnEntity {
            return from(entity.id, entity.uuid, entity.type, entity.position, entity.headYaw(), 0, entity.velocity)
        }

        @JvmStatic
        fun from(entityId: Int, uuid: UUID, type: KryptonEntityType<*>, pos: Position, headYaw: Float, data: Int,
                 velocity: Vec3d): PacketOutSpawnEntity {
            val pitch = Positioning.encodeRotation(pos.pitch)
            val yaw = Positioning.encodeRotation(pos.yaw)
            val encodedHeadYaw = Positioning.encodeRotation(headYaw)
            val dx = Positioning.encodeVelocity(velocity.x)
            val dy = Positioning.encodeVelocity(velocity.y)
            val dz = Positioning.encodeVelocity(velocity.z)
            return PacketOutSpawnEntity(entityId, uuid, type, pos.x, pos.y, pos.z, pitch, yaw, encodedHeadYaw, data, dx, dy, dz)
        }
    }
}
