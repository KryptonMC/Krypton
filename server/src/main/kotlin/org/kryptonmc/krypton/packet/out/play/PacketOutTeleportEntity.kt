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
import org.kryptonmc.krypton.coordinate.Positioning
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.packet.MovementPacket

@JvmRecord
data class PacketOutTeleportEntity(
    override val entityId: Int,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Byte,
    val pitch: Byte,
    override val onGround: Boolean
) : EntityPacket, MovementPacket {

    constructor(reader: BinaryReader) : this(reader.readVarInt(), reader.readDouble(), reader.readDouble(), reader.readDouble(), reader.readByte(),
        reader.readByte(), reader.readBoolean())

    override fun write(writer: BinaryWriter) {
        writer.writeVarInt(entityId)
        writer.writeDouble(x)
        writer.writeDouble(y)
        writer.writeDouble(z)
        writer.writeByte(yaw)
        writer.writeByte(pitch)
        writer.writeBoolean(onGround)
    }

    companion object {

        @JvmStatic
        fun create(entity: KryptonEntity): PacketOutTeleportEntity = from(entity.id, entity.position, entity.isOnGround)

        @JvmStatic
        fun from(entityId: Int, position: Position, onGround: Boolean): PacketOutTeleportEntity {
            val yaw = Positioning.encodeRotation(position.yaw)
            val pitch = Positioning.encodeRotation(position.pitch)
            return PacketOutTeleportEntity(entityId, position.x, position.y, position.z, yaw, pitch, onGround)
        }
    }
}
