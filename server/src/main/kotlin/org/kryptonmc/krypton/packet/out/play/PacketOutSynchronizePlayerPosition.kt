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
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeVarInt
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

    constructor(buf: ByteBuf) : this(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), buf.readFloat(),
        RelativeArgument.unpack(buf.readUnsignedByte().toInt()), buf.readVarInt(), buf.readBoolean())

    override fun write(buf: ByteBuf) {
        buf.writeDouble(x)
        buf.writeDouble(y)
        buf.writeDouble(z)
        buf.writeFloat(yaw)
        buf.writeFloat(pitch)
        buf.writeByte(RelativeArgument.pack(relativeArguments))
        buf.writeVarInt(teleportId)
        buf.writeBoolean(shouldDismount)
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
        fun fromPlayer(player: KryptonPlayer): PacketOutSynchronizePlayerPosition =
            PacketOutSynchronizePlayerPosition(player.position.x, player.position.y, player.position.z, player.yaw, player.pitch)
    }
}
