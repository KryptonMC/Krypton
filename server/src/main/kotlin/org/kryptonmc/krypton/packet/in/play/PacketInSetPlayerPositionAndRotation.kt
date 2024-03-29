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
package org.kryptonmc.krypton.packet.`in`.play

import org.kryptonmc.api.util.Position
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.network.handlers.PlayPacketHandler
import org.kryptonmc.krypton.packet.InboundPacket
import org.kryptonmc.krypton.packet.MovementPacket

@JvmRecord
data class PacketInSetPlayerPositionAndRotation(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
    override val onGround: Boolean
) : MovementPacket, InboundPacket<PlayPacketHandler> {

    constructor(reader: BinaryReader) : this(reader.readDouble(), reader.readDouble(), reader.readDouble(), reader.readFloat(), reader.readFloat(),
        reader.readBoolean())

    fun position(): Position = Position(x, y, z, yaw, pitch)

    override fun write(writer: BinaryWriter) {
        writer.writeDouble(x)
        writer.writeDouble(y)
        writer.writeDouble(z)
        writer.writeFloat(yaw)
        writer.writeFloat(pitch)
        writer.writeBoolean(onGround)
    }

    override fun handle(handler: PlayPacketHandler) {
        handler.handlePlayerPositionAndRotation(this)
    }
}
