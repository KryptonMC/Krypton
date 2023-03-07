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
package org.kryptonmc.krypton.world.chunk.data

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.coordinate.SectionPos
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.biome.NoiseBiomeSource
import org.kryptonmc.krypton.world.block.KryptonBlocks
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

    constructor(y: Int, biomeRegistry: KryptonRegistry<Biome>) : this(
        y,
        PaletteHolder(PaletteHolder.Strategy.BLOCKS, KryptonBlocks.AIR.defaultState),
        PaletteHolder(PaletteHolder.Strategy.biomes(biomeRegistry), biomeRegistry.get(BiomeKeys.PLAINS)!!),
        null,
        null
    )

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

    fun write(buf: ByteBuf) {
        buf.writeShort(nonEmptyBlockCount)
        blocks.write(buf)
        biomes.write(buf)
    }

    override fun getNoiseBiome(x: Int, y: Int, z: Int): Biome = biomes.get(x, y, z)
}
