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
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.EntityPacket
import java.util.UUID

/**
 * Spawn a player for the client.
 */
@JvmRecord
data class PacketOutSpawnPlayer(
    override val entityId: Int,
    val uuid: UUID,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Byte,
    val pitch: Byte
) : EntityPacket {

    constructor(reader: BinaryReader) : this(reader.readVarInt(), reader.readUUID(), reader.readDouble(), reader.readDouble(), reader.readDouble(),
        reader.readByte(), reader.readByte())

    override fun write(writer: BinaryWriter) {
        writer.writeVarInt(entityId)
        writer.writeUUID(uuid)
        writer.writeDouble(x)
        writer.writeDouble(y)
        writer.writeDouble(z)
        writer.writeByte(yaw)
        writer.writeByte(pitch)
    }

    companion object {

        @JvmStatic
        fun create(player: KryptonPlayer): PacketOutSpawnPlayer = from(player.id, player.uuid, player.position)

        @JvmStatic
        fun from(entityId: Int, uuid: UUID, pos: Position): PacketOutSpawnPlayer {
            val pitch = Positioning.encodeRotation(pos.pitch)
            val yaw = Positioning.encodeRotation(pos.yaw)
            return PacketOutSpawnPlayer(entityId, uuid, pos.x, pos.y, pos.z, yaw, pitch)
        }
    }
}
