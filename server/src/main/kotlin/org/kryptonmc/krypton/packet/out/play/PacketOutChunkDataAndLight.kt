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
import org.kryptonmc.krypton.util.writeNBT
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.nbt.CompoundTag

class PacketOutChunkDataAndLight(
    chunk: KryptonChunk,
    trustEdges: Boolean = true
) : PacketOutLight(chunk, trustEdges) {

    private val heightmaps: CompoundTag
    private val buffer: ByteArray

    init {
        val heightmapData = CompoundTag.builder()
        chunk.heightmaps.forEach {
            if (it.key.sendToClient) heightmapData.longArray(it.key.name, it.value.data.data)
        }
        heightmaps = heightmapData.build()

        buffer = ByteArray(chunk.sections.sumOf { it.serializedSize })
        val wrappedBuffer = Unpooled.wrappedBuffer(buffer).writerIndex(0)
        chunk.sections.forEach { it.write(wrappedBuffer) }
    }

    override fun write(buf: ByteBuf) {
        buf.writeInt(chunk.position.x)
        buf.writeInt(chunk.position.z)

        // Heightmaps
        buf.writeNBT(heightmaps)

        // Actual chunk data
        buf.writeVarInt(buffer.size)
        buf.writeBytes(buffer)

        // TODO: When block entities are added, make use of this here
        buf.writeVarInt(0) // number of block entities

        // Light data from super
        super.write(buf)
    }
}
