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

import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.world.KryptonWorldBorder

@JvmRecord
data class PacketOutInitializeWorldBorder(
    val centerX: Double,
    val centerZ: Double,
    val oldSize: Double,
    val newSize: Double,
    val speed: Long,
    val teleportBoundary: Int,
    val warningBlocks: Int,
    val warningTime: Int
) : Packet {

    constructor(reader: BinaryReader) : this(reader.readDouble(), reader.readDouble(), reader.readDouble(), reader.readDouble(),
        reader.readVarLong(), reader.readVarInt(), reader.readVarInt(), reader.readVarInt())

    override fun write(writer: BinaryWriter) {
        writer.writeDouble(centerX)
        writer.writeDouble(centerZ)
        writer.writeDouble(oldSize)
        writer.writeDouble(newSize)
        writer.writeVarLong(speed)
        writer.writeVarInt(teleportBoundary)
        writer.writeVarInt(warningBlocks)
        writer.writeVarInt(warningTime)
    }

    companion object {

        private const val PORTAL_TELEPORT_BOUNDARY = 29999984

        @JvmStatic
        fun create(border: KryptonWorldBorder): PacketOutInitializeWorldBorder {
            return PacketOutInitializeWorldBorder(border.centerX, border.centerZ, border.size, border.size, 0, PORTAL_TELEPORT_BOUNDARY,
                border.warningBlocks, border.warningTime)
        }
    }
}
