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

import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.random.RandomSource
import java.util.EnumSet

@JvmRecord
data class PacketOutSynchronizePlayerPosition(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
    val relativeArguments: Set<RelativeArgument> = emptySet(),
    val teleportId: Int = RANDOM.nextInt(RANDOM_TELEPORT_ID_UPPER_BOUND),
    val shouldDismount: Boolean = false
) : Packet {

    constructor(reader: BinaryReader) : this(reader.readDouble(), reader.readDouble(), reader.readDouble(), reader.readFloat(), reader.readFloat(),
        RelativeArgument.unpack(reader.readByte().toInt()), reader.readVarInt(), reader.readBoolean())

    override fun write(writer: BinaryWriter) {
        writer.writeDouble(x)
        writer.writeDouble(y)
        writer.writeDouble(z)
        writer.writeFloat(yaw)
        writer.writeFloat(pitch)
        writer.writeByte(RelativeArgument.pack(relativeArguments).toByte())
        writer.writeVarInt(teleportId)
        writer.writeBoolean(shouldDismount)
    }

    enum class RelativeArgument(private val bit: Int) {

        X(0),
        Y(1),
        Z(2),
        PITCH(3),
        YAW(4);

        private fun mask(): Int = 1 shl bit

        private fun isSet(flags: Int): Boolean = flags and mask() == mask()

        companion object {

            private val VALUES = values()

            @JvmStatic
            fun unpack(flags: Int): Set<RelativeArgument> {
                val result = EnumSet.noneOf(RelativeArgument::class.java)
                VALUES.forEach { if (it.isSet(flags)) result.add(it) }
                return result
            }

            @JvmStatic
            fun pack(arguments: Set<RelativeArgument>): Int {
                var result = 0
                arguments.forEach { result = result or it.mask() }
                return result
            }
        }
    }

    companion object {

        private val RANDOM = RandomSource.createThreadSafe()
        private const val RANDOM_TELEPORT_ID_UPPER_BOUND = 1000 // A number I chose because it seems sensible enough

        @JvmStatic
        fun fromPlayer(player: KryptonPlayer): PacketOutSynchronizePlayerPosition {
            return PacketOutSynchronizePlayerPosition(player.position.x, player.position.y, player.position.z, player.position.yaw,
                player.position.pitch)
        }
    }
}
