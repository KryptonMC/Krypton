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

import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.util.Quart
import org.kryptonmc.krypton.world.components.HeightAccessor
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.biome.NoiseBiomeSource
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.components.BlockGetter
import java.util.EnumMap
import java.util.EnumSet

abstract class ChunkAccessor(
    val position: ChunkPos,
    private val heightAccessor: HeightAccessor,
    var inhabitedTime: Long,
    sections: Array<ChunkSection?>?
) : BlockGetter, NoiseBiomeSource {

    abstract val status: ChunkStatus

    private val sectionArray = arrayOfNulls<ChunkSection>(heightAccessor.sectionCount)
    val heightmaps: MutableMap<Heightmap.Type, Heightmap> = EnumMap(Heightmap.Type::class.java)

    val sections: Array<ChunkSection>
        @Suppress("UNCHECKED_CAST") get() = sectionArray as Array<ChunkSection>
    val highestSectionY: Int
        get() = highestSection()?.bottomBlockY ?: minimumBuildHeight
    override val height: Int
        get() = heightAccessor.height
    override val minimumBuildHeight: Int
        get() = heightAccessor.minimumBuildHeight

    init {
        if (sections != null) {
            if (this.sectionArray.size == sections.size) {
                System.arraycopy(sections, 0, this.sectionArray, 0, this.sectionArray.size)
            } else {
                LOGGER.warn("Failed to set chunk sections! Expected array size ${this.sectionArray.size} but got ${sections.size}!")
            }
        }
        replaceMissingSections(heightAccessor, this.sectionArray)
    }

    abstract fun setBlock(pos: BlockPos, state: KryptonBlockState, moving: Boolean): KryptonBlockState?

    private fun section(index: Int): ChunkSection = sectionArray[index]!!

    fun getOrCreateHeightmap(type: Heightmap.Type): Heightmap = heightmaps.computeIfAbsent(type) { Heightmap(this, it) }

    fun setHeightmap(type: Heightmap.Type, data: LongArray) {
        getOrCreateHeightmap(type).setData(this, type, data)
    }

    fun getHeight(type: Heightmap.Type, x: Int, z: Int): Int {
        var heightmap = heightmaps.get(type)
        if (heightmap == null) {
            Heightmap.prime(this, EnumSet.of(type))
            heightmap = heightmaps.get(type)!!
        }
        return heightmap.firstAvailable(x and 15, z and 15) - 1
    }

    override fun getNoiseBiome(x: Int, y: Int, z: Int): Biome {
        val minimumQuart = Quart.fromBlock(minimumBuildHeight)
        val maximumQuart = minimumQuart + Quart.fromBlock(height) - 1
        val actualY = Maths.clamp(y, minimumQuart, maximumQuart)
        val sectionIndex = getSectionIndex(Quart.toBlock(actualY))
        return section(sectionIndex).getNoiseBiome(x and 3, actualY and 3, z and 3)
    }

    private fun highestSection(): ChunkSection? {
        for (i in sectionArray.size - 1 downTo 0) {
            val section = sectionArray[i]
            if (!section!!.hasOnlyAir()) return section
        }
        return null
    }

    companion object {

        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        private fun replaceMissingSections(heightAccessor: HeightAccessor, sections: Array<ChunkSection?>) {
            for (i in sections.indices) {
                if (sections[i] == null) sections[i] = ChunkSection(heightAccessor.getSectionYFromSectionIndex(i))
            }
        }
    }
}
