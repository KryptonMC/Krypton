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
import io.netty.buffer.Unpooled
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeBitSet
import org.kryptonmc.krypton.util.writeIntArray
import org.kryptonmc.krypton.util.writeNBT
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.nbt.CompoundTag
import java.util.BitSet

data class PacketOutChunkData(val chunk: KryptonChunk) : Packet {

    private val buffer = ByteArray(chunk.calculateSize())
    private val sectionMask = chunk.extract(Unpooled.wrappedBuffer(buffer).apply { writerIndex(0) })

    override fun write(buf: ByteBuf) {
        buf.writeInt(chunk.position.x)
        buf.writeInt(chunk.position.z)
        buf.writeBitSet(sectionMask)

        // Heightmaps
        val heightmaps = CompoundTag.builder()
        chunk.heightmaps.forEach { if (it.key.sendToClient) heightmaps.longArray(it.key.name, it.value.data.data) }
        buf.writeNBT(heightmaps.build())

        // Biomes
        buf.writeIntArray(chunk.biomes.write())

        // Actual chunk data
        buf.writeVarInt(buffer.size)
        buf.writeBytes(buffer)

        // TODO: When block entities are added, make use of this here
        buf.writeVarInt(0) // number of block entities
    }

    private fun KryptonChunk.extract(buf: ByteBuf): BitSet {
        val mask = BitSet()
        for (i in sections.indices) {
            val section = sections[i]
            if (section != null && !section.isEmpty()) {
                mask.set(i)
                section.write(buf)
            }
        }
        return mask
    }

    private fun KryptonChunk.calculateSize(): Int {
        var size = 0
        for (i in sections.indices) {
            val section = sections[i]
            if (section != null && !section.isEmpty()) size += section.serializedSize
        }
        return size
    }
}
