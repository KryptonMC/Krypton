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
package org.kryptonmc.krypton.packet.out.play.data

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import java.util.Objects
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.readNBT
import org.kryptonmc.krypton.util.readVarIntByteArray
import org.kryptonmc.krypton.util.writeNBT
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVarIntByteArray
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.nbt.CompoundTag

@JvmRecord
data class ChunkPacketData(val heightmaps: CompoundTag, val data: ByteArray) : Writable {

    constructor(chunk: KryptonChunk) : this(extractHeightmaps(chunk), extractData(chunk))

    constructor(buf: ByteBuf) : this(buf.readNBT(), buf.readVarIntByteArray())

    override fun write(buf: ByteBuf) {
        buf.writeNBT(heightmaps) // Heightmaps
        buf.writeVarIntByteArray(data) // Actual chunk data

        // TODO: When block entities are added, make use of this here
        buf.writeVarInt(0) // Number of block entities
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return heightmaps == (other as ChunkPacketData).heightmaps && data.contentEquals(other.data)
    }

    override fun hashCode(): Int = Objects.hash(heightmaps, data)

    companion object {

        @JvmStatic
        private fun extractHeightmaps(chunk: KryptonChunk): CompoundTag {
            if (chunk.heightmaps.isEmpty()) return CompoundTag.empty()
            val heightmaps = CompoundTag.immutableBuilder()
            chunk.heightmaps.forEach { if (it.key.sendToClient()) heightmaps.longArray(it.key.name, it.value.rawData) }
            return heightmaps.build()
        }

        @JvmStatic
        private fun extractData(chunk: KryptonChunk): ByteArray {
            val result = ByteArray(chunk.sections.sumOf { it.calculateSerializedSize() })
            val buffer = Unpooled.wrappedBuffer(result).writerIndex(0)
            chunk.sections.forEach { it.write(buffer) }
            return result
        }
    }
}
