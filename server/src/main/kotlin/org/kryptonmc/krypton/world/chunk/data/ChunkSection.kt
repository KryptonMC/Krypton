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
package org.kryptonmc.krypton.world.chunk.data

import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.coordinate.SectionPos
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.world.biome.NoiseBiomeSource
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.block.palette.PaletteHolder

/**
 * A section of a chunk. These are 16x16x16 areas that hold the actual block
 * states and palette information.
 */
class ChunkSection(
    y: Int,
    val blocks: PaletteHolder<KryptonBlockState>,
    val biomes: PaletteHolder<Biome>,
    val blockLight: ByteArray?,
    val skyLight: ByteArray?
) : NoiseBiomeSource {

    val bottomBlockY: Int = SectionPos.sectionToBlock(y)
    private var nonEmptyBlockCount = 0

    init {
        recount()
    }

    constructor(y: Int, biomeRegistry: KryptonRegistry<Biome>) : this(y, PaletteHolder.blocks(), PaletteHolder.biomes(biomeRegistry), null, null)

    fun getBlock(x: Int, y: Int, z: Int): KryptonBlockState = blocks.get(x, y, z)

    fun setBlock(x: Int, y: Int, z: Int, block: KryptonBlockState): KryptonBlockState {
        val oldBlock = blocks.getAndSet(x, y, z, block)
        if (!oldBlock.isAir()) nonEmptyBlockCount--
        if (!block.isAir()) nonEmptyBlockCount++
        return oldBlock
    }

    fun hasOnlyAir(): Boolean = nonEmptyBlockCount == 0

    fun calculateSerializedSize(): Int = 2 + blocks.calculateSerializedSize() + biomes.calculateSerializedSize()

    private fun recount() {
        nonEmptyBlockCount = 0
        blocks.forEachLocation { block, _ ->
            val fluid = block.asFluid()
            if (!block.isAir()) nonEmptyBlockCount++
            if (!fluid.isEmpty()) nonEmptyBlockCount++
        }
    }

    fun write(writer: BinaryWriter) {
        writer.writeShort(nonEmptyBlockCount.toShort())
        blocks.write(writer)
        biomes.write(writer)
    }

    override fun getNoiseBiome(x: Int, y: Int, z: Int): Biome = biomes.get(x, y, z)
}
