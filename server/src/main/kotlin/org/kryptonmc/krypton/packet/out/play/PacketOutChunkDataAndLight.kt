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
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.data.ChunkPacketData
import org.kryptonmc.krypton.packet.out.play.data.LightPacketData
import org.kryptonmc.krypton.world.chunk.KryptonChunk

@JvmRecord
data class PacketOutChunkDataAndLight(val x: Int, val z: Int, val chunkData: ChunkPacketData, val lightData: LightPacketData) : Packet {

    constructor(chunk: KryptonChunk, trustEdges: Boolean) : this(chunk.position.x, chunk.position.z, ChunkPacketData(chunk),
        LightPacketData.create(chunk, trustEdges))

    constructor(buf: ByteBuf) : this(buf.readInt(), buf.readInt(), ChunkPacketData(buf), LightPacketData(buf))

    override fun write(buf: ByteBuf) {
        buf.writeInt(x)
        buf.writeInt(z)
        chunkData.write(buf)
        lightData.write(buf)
    }
}
