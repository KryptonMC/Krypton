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
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeBitSet
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import java.util.BitSet

/**
 * Update the light levels for the specified [chunk]
 *
 * @param chunk the chunk to update the light levels for
 */
// TODO: Fix light when we add the new engine
class PacketOutUpdateLight(private val chunk: KryptonChunk) : PlayPacket(0x25) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(chunk.position.x)
        buf.writeVarInt(chunk.position.z)
        buf.writeBoolean(true)
        val sections = chunk.sections

        val skyLightMask = BitSet()
        val blockLightMask = BitSet()
        val emptySkyLightMask = BitSet()
        val emptyBlockLightMask = BitSet()
        for (i in sections.indices) {
            val section = sections[i]
            if (section == null) {
                skyLightMask.set(i, false)
                emptySkyLightMask.set(i, true)
                blockLightMask.set(i, false)
                emptyBlockLightMask.set(i, true)
                continue
            }

            val hasSkyLight = section.skyLight.any { it != 0.toByte() }
            skyLightMask.set(i, hasSkyLight)
            emptySkyLightMask.set(i, !hasSkyLight)

            val hasBlockLight = section.blockLight.any { it != 0.toByte() }
            blockLightMask.set(i, hasBlockLight)
            emptyBlockLightMask.set(i, !hasBlockLight)
        }
        buf.writeBitSet(skyLightMask)
        buf.writeBitSet(blockLightMask)
        buf.writeBitSet(emptySkyLightMask)
        buf.writeBitSet(emptyBlockLightMask)

        sections.asSequence().filterNotNull().filter { section -> !section.skyLight.all { it == 0.toByte() } }.apply { buf.writeVarInt(count()) }.forEach {
            buf.writeVarInt(2048)
            buf.writeBytes(it.skyLight)
        }
        sections.asSequence().filterNotNull().filter { section -> !section.blockLight.all { it == 0.toByte() } }.apply { buf.writeVarInt(count()) }.forEach {
            buf.writeVarInt(2048)
            buf.writeBytes(it.blockLight)
        }
    }
}
