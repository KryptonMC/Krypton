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
package org.kryptonmc.krypton.packet.out.play.data

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.readNBT
import org.kryptonmc.krypton.util.readVarIntByteArray
import org.kryptonmc.krypton.util.writeNBT
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVarIntByteArray
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ImmutableCompoundTag

@JvmRecord
@Suppress("ArrayInDataClass")
data class ChunkPacketData(val heightmaps: CompoundTag, val data: ByteArray) : Writable {

    constructor(buf: ByteBuf) : this(buf.readNBT(), buf.readVarIntByteArray())

    override fun write(buf: ByteBuf) {
        buf.writeNBT(heightmaps) // Heightmaps
        buf.writeVarIntByteArray(data) // Actual chunk data

        // TODO: When block entities are added, make use of this here
        buf.writeVarInt(0) // Number of block entities
    }

    companion object {

        @JvmStatic
        fun fromChunk(chunk: KryptonChunk): ChunkPacketData = ChunkPacketData(extractHeightmaps(chunk), extractData(chunk))

        @JvmStatic
        private fun extractHeightmaps(chunk: KryptonChunk): CompoundTag {
            if (chunk.heightmaps.isEmpty()) return CompoundTag.EMPTY
            val heightmaps = ImmutableCompoundTag.builder()
            chunk.heightmaps.forEach { if (it.key.sendToClient()) heightmaps.putLongArray(it.key.name, it.value.rawData()) }
            return heightmaps.build()
        }

        @JvmStatic
        private fun extractData(chunk: KryptonChunk): ByteArray {
            val result = ByteArray(chunk.sections().sumOf { it.calculateSerializedSize() })
            val buffer = Unpooled.wrappedBuffer(result).writerIndex(0)
            chunk.sections().forEach { it.write(buffer) }
            return result
        }
    }
}
