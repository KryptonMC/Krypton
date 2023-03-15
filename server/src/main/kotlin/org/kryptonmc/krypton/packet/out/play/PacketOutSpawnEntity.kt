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

import org.kryptonmc.api.util.Position
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.coordinate.Positioning
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
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
    val velocityX: Short,
    val velocityY: Short,
    val velocityZ: Short
) : EntityPacket {

    constructor(reader: BinaryReader) : this(reader.readVarInt(), reader.readUUID(), reader.readById(KryptonRegistries.ENTITY_TYPE)!!,
        reader.readDouble(), reader.readDouble(), reader.readDouble(), reader.readByte(), reader.readByte(), reader.readByte(), reader.readVarInt(),
        reader.readShort(), reader.readShort(), reader.readShort())

    override fun write(writer: BinaryWriter) {
        writer.writeVarInt(entityId)
        writer.writeUUID(uuid)
        writer.writeId(KryptonRegistries.ENTITY_TYPE, type)
        writer.writeDouble(x)
        writer.writeDouble(y)
        writer.writeDouble(z)
        writer.writeByte(pitch)
        writer.writeByte(yaw)
        writer.writeByte(headYaw)
        writer.writeVarInt(data)
        writer.writeShort(velocityX)
        writer.writeShort(velocityY)
        writer.writeShort(velocityZ)
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
