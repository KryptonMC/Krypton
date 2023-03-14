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
import org.kryptonmc.krypton.coordinate.Positioning
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
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

    constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readUUID(), buf.readDouble(), buf.readDouble(), buf.readDouble(),
        buf.readByte(), buf.readByte())

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeUUID(uuid)
        buf.writeDouble(x)
        buf.writeDouble(y)
        buf.writeDouble(z)
        buf.writeByte(yaw.toInt())
        buf.writeByte(pitch.toInt())
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
