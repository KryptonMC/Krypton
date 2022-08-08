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
package org.kryptonmc.krypton.world.chunk

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.Biomes
import org.kryptonmc.krypton.world.biome.NoiseBiomeSource
import org.kryptonmc.krypton.world.block.downcast
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.block.palette.PaletteHolder

/**
 * A section of a chunk. These are 16x16x16 areas that hold the actual block
 * states and palette information.
 */
class ChunkSection(
    y: Int,
    val blocks: PaletteHolder<KryptonBlockState> = PaletteHolder(PaletteHolder.Strategy.BLOCKS, Blocks.AIR.defaultState.downcast()),
    val biomes: PaletteHolder<Biome> = PaletteHolder(PaletteHolder.Strategy.BIOMES, Biomes.PLAINS),
    val blockLight: ByteArray = ByteArray(LIGHTS_SIZE),
    val skyLight: ByteArray = ByteArray(LIGHTS_SIZE)
) : NoiseBiomeSource {

    val bottomBlockY: Int = y shl 4
    private var nonEmptyBlockCount = 0

    init {
        recount()
    }

    fun get(x: Int, y: Int, z: Int): KryptonBlockState = blocks[x, y, z]

    fun set(x: Int, y: Int, z: Int, block: KryptonBlockState): KryptonBlockState {
        val oldBlock = blocks.getAndSet(x, y, z, block)
        if (!oldBlock.isAir) nonEmptyBlockCount--
        if (!block.isAir) nonEmptyBlockCount++
        return oldBlock
    }

    fun hasOnlyAir(): Boolean = nonEmptyBlockCount == 0

    fun calculateSerializedSize(): Int = 2 + blocks.calculateSerializedSize() + biomes.calculateSerializedSize()

    private fun recount() {
        nonEmptyBlockCount = 0
        blocks.forEachLocation { block, _ ->
            val fluid = block.asFluid()
            if (!block.isAir) nonEmptyBlockCount++
            if (!fluid.isEmpty) nonEmptyBlockCount++
        }
    }

    fun write(buf: ByteBuf) {
        buf.writeShort(nonEmptyBlockCount)
        blocks.write(buf)
        biomes.write(buf)
    }

    override fun getNoiseBiome(x: Int, y: Int, z: Int): Biome = biomes[x, y, z]

    companion object {

        private const val LIGHTS_SIZE = 2048
    }
}
