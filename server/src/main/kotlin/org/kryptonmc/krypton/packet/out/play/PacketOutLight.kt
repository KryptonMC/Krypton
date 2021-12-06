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

@Suppress("MemberVisibilityCanBePrivate") // We expose the data so it can be read by packet interceptors
sealed class PacketOutLight(
    val chunk: KryptonChunk,
    val trustEdges: Boolean = true
) : Packet {

    val skyMask: Long
    val blockMask: Long
    val emptySkyMask: Long
    val emptyBlockMask: Long
    val skyLights = ArrayList<ByteArray>()
    val blockLights = ArrayList<ByteArray>()

    init {
        val sections = chunk.sections

        var tempSkyMask = 0L
        var tempBlockMask = 0L
        var tempEmptySkyMask = 0L
        var tempEmptyBlockMask = 0L

        for (i in sections.indices) {
            val section = sections[i]
            if (section.hasOnlyAir()) {
                tempEmptySkyMask = tempEmptySkyMask or (1L shl i)
                tempEmptyBlockMask = tempEmptyBlockMask or (1L shl i)
                continue
            }

            // Deal with sky light data
            if (section.skyLight.hasNonZeroData()) {
                tempSkyMask = tempSkyMask or (1L shl i)
                skyLights.add(section.skyLight)
            } else {
                tempEmptySkyMask = tempEmptySkyMask or (1L shl i)
            }

            // Deal with block light data
            if (section.blockLight.hasNonZeroData()) {
                tempBlockMask = tempBlockMask or (1L shl i)
                blockLights.add(section.blockLight)
            } else {
                tempEmptyBlockMask = tempEmptyBlockMask or (1L shl i)
            }
        }
        skyMask = tempSkyMask
        blockMask = tempBlockMask
        emptySkyMask = tempEmptySkyMask
        emptyBlockMask = tempEmptyBlockMask
    }

    override fun write(buf: ByteBuf) {
        buf.writeBoolean(trustEdges)

        buf.writeSingletonLongArray(skyMask)
        buf.writeSingletonLongArray(blockMask)
        buf.writeSingletonLongArray(emptySkyMask)
        buf.writeSingletonLongArray(emptyBlockMask)

        buf.writeVarInt(skyLights.size)
        for (i in skyLights.indices) {
            val light = skyLights[i]
            buf.writeVarInt(light.size)
            buf.writeBytes(light)
        }

        buf.writeVarInt(blockLights.size)
        for (i in blockLights.indices) {
            val light = blockLights[i]
            buf.writeVarInt(light.size)
            buf.writeBytes(light)
        }
    }

    companion object {

        @JvmStatic
        private fun ByteArray.hasNonZeroData(): Boolean {
            for (i in indices) {
                if (get(i) != 0.toByte()) return true
            }
            return false
        }
    }
}
