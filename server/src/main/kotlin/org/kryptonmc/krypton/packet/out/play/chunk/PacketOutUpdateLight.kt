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
package org.kryptonmc.krypton.packet.out.play.chunk

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.chunk.KryptonChunk

/**
 * Update the light levels for the specified [chunk]
 *
 * @param chunk the chunk to update the light levels for
 */
class PacketOutUpdateLight(private val chunk: KryptonChunk) : PlayPacket(0x23) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(chunk.position.x)
        buf.writeVarInt(chunk.position.z)
        buf.writeBoolean(true)
        val sections = chunk.sections.sortedBy { it.y }

        var skyLightMask = 0
        var blockLightMask = 0
        var emptySkyLightMask = 0
        var emptyBlockLightMask = 0
        for (i in sections.indices) {
            val section = sections[i]

            val skyMask = if (section.skyLight.all { it == 0.toByte() }) 0 else 1
            emptySkyLightMask += -skyMask + 1 shl i
            skyLightMask += skyMask shl i

            val blockMask = if (section.blockLight.all { it == 0.toByte() }) 0 else 1
            emptyBlockLightMask += -blockMask + 1 shl i
            blockLightMask += blockMask shl i
        }
        buf.writeVarInt(skyLightMask)
        buf.writeVarInt(blockLightMask)
        buf.writeVarInt(emptySkyLightMask)
        buf.writeVarInt(emptyBlockLightMask)

        sections.filter { section -> !section.skyLight.all { it == 0.toByte() } }.forEach {
            buf.writeVarInt(2048)
            buf.writeBytes(it.skyLight)
        }
        sections.filter { section -> !section.blockLight.all { it == 0.toByte() } }.forEach {
            buf.writeVarInt(2048)
            buf.writeBytes(it.blockLight)
        }
    }
}
