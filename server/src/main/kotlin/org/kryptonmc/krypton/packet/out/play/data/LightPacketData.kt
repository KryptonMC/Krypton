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

import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import java.util.BitSet

@JvmRecord
data class LightPacketData(
    val trustEdges: Boolean,
    val skyMask: BitSet,
    val blockMask: BitSet,
    val emptySkyMask: BitSet,
    val emptyBlockMask: BitSet,
    val skyLights: List<ByteArray>,
    val blockLights: List<ByteArray>
) : Writable {

    constructor(reader: BinaryReader) : this(
        reader.readBoolean(),
        reader.readBitSet(),
        reader.readBitSet(),
        reader.readBitSet(),
        reader.readBitSet(),
        reader.readList { it.readByteArray() },
        reader.readList { it.readByteArray() }
    )

    override fun write(writer: BinaryWriter) {
        writer.writeBoolean(trustEdges)

        writer.writeBitSet(skyMask)
        writer.writeBitSet(blockMask)
        writer.writeBitSet(emptySkyMask)
        writer.writeBitSet(emptyBlockMask)

        writer.writeCollection(skyLights, writer::writeByteArray)
        writer.writeCollection(blockLights, writer::writeByteArray)
    }

    companion object {

        @JvmStatic
        fun fromChunk(chunk: KryptonChunk, trustEdges: Boolean): LightPacketData {
            val sections = chunk.sections()

            val skyMask = BitSet()
            val blockMask = BitSet()
            val emptySkyMask = BitSet()
            val emptyBlockMask = BitSet()
            val skyLights = ArrayList<ByteArray>()
            val blockLights = ArrayList<ByteArray>()

            for (i in sections.indices) {
                val section = sections[i]

                // Deal with sky light data
                if (hasNonZeroData(section.skyLight)) {
                    skyMask.set(i)
                    skyLights.add(section.skyLight!!)
                } else {
                    emptySkyMask.set(i)
                }

                // Deal with block light data
                if (hasNonZeroData(section.blockLight)) {
                    blockMask.set(i)
                    blockLights.add(section.blockLight!!)
                } else {
                    emptyBlockMask.set(i)
                }
            }

            return LightPacketData(trustEdges, skyMask, blockMask, emptySkyMask, emptyBlockMask, skyLights, blockLights)
        }

        @JvmStatic
        private fun hasNonZeroData(array: ByteArray?): Boolean {
            if (array == null) return false
            for (i in array.indices) {
                if (array[i] != 0.toByte()) return true
            }
            return false
        }
    }
}
