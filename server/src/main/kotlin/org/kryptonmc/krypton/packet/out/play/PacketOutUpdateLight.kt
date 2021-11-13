/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeSingletonLongArray
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.chunk.KryptonChunk

@JvmRecord
data class PacketOutUpdateLight(
    val chunk: KryptonChunk,
    val trustEdges: Boolean = true
) : Packet {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(chunk.position.x)
        buf.writeVarInt(chunk.position.z)
        buf.writeBoolean(trustEdges)
        val sections = chunk.sections

        var skyMask = 0L
        var blockMask = 0L
        var emptySkyMask = 0L
        var emptyBlockMask = 0L
        val skyLights = ArrayList<ByteArray>()
        val blockLights = ArrayList<ByteArray>()

        for (i in sections.indices) {
            val section = sections[i]
            if (section == null) {
                emptySkyMask = emptySkyMask or (1L shl i)
                emptyBlockMask = emptyBlockMask or (1L shl i)
                continue
            }

            // Deal with sky light data
            if (section.skyLight.hasNonZeroData()) {
                skyMask = skyMask or (1L shl i)
                skyLights.add(section.skyLight)
            } else {
                emptySkyMask = emptySkyMask or (1L shl i)
            }

            // Deal with block light data
            if (section.blockLight.hasNonZeroData()) {
                blockMask = blockMask or (1L shl i)
                blockLights.add(section.blockLight)
            } else {
                emptyBlockMask = emptyBlockMask or (1L shl i)
            }
        }

        buf.writeSingletonLongArray(skyMask)
        buf.writeSingletonLongArray(blockMask)
        buf.writeSingletonLongArray(emptySkyMask)
        buf.writeSingletonLongArray(emptyBlockMask)

        buf.writeVarInt(skyLights.size)
        for (i in skyLights.indices) {
            val light = skyLights[i]
            buf.writeVarInt(2048) // Always 2048 in length (for now)
            buf.writeBytes(light)
        }

        buf.writeVarInt(blockLights.size)
        for (i in blockLights.indices) {
            val light = blockLights[i]
            buf.writeVarInt(2048)
            buf.writeBytes(light)
        }
    }

    companion object {

        private fun ByteArray.hasNonZeroData(): Boolean {
            for (i in indices) {
                if (get(i) != 0.toByte()) return true
            }
            return false
        }
    }
}
