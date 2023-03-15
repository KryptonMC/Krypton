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

import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.data.ChunkPacketData
import org.kryptonmc.krypton.packet.out.play.data.LightPacketData
import org.kryptonmc.krypton.world.chunk.KryptonChunk

@JvmRecord
data class PacketOutChunkDataAndLight(val x: Int, val z: Int, val chunkData: ChunkPacketData, val lightData: LightPacketData) : Packet {

    constructor(reader: BinaryReader) : this(reader.readInt(), reader.readInt(), ChunkPacketData(reader), LightPacketData(reader))

    override fun write(writer: BinaryWriter) {
        writer.writeInt(x)
        writer.writeInt(z)
        chunkData.write(writer)
        lightData.write(writer)
    }

    companion object {

        @JvmStatic
        fun fromChunk(chunk: KryptonChunk, trustEdges: Boolean): PacketOutChunkDataAndLight {
            return PacketOutChunkDataAndLight(chunk.position.x, chunk.position.z, ChunkPacketData.fromChunk(chunk),
                LightPacketData.fromChunk(chunk, trustEdges))
        }
    }
}
