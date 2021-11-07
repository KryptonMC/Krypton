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
package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.util.Quart
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.world.BlockAccessor
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.biome.NoiseBiomeSource

interface ChunkAccessor : BlockAccessor, NoiseBiomeSource {

    val position: ChunkPosition
    val status: ChunkStatus
    var isUnsaved: Boolean
    var inhabitedTime: Long
    var isLightCorrect: Boolean
    val sections: Array<ChunkSection?>
    val heightmaps: Map<Heightmap.Type, Heightmap>

    val highestSection: ChunkSection?
        get() = sections.lastOrNull { it != null && !it.isEmpty() }
    val highestSectionY: Int
        get() = highestSection?.bottomBlockY ?: minimumBuildHeight

    fun getOrCreateSection(index: Int) = sections.getOrNull(index)
        ?: ChunkSection(sectionYFromIndex(index)).apply { sections[index] = this }

    fun getOrCreateHeightmap(type: Heightmap.Type): Heightmap

    fun getHeight(type: Heightmap.Type, x: Int, z: Int): Int

    fun setHeightmap(type: Heightmap.Type, data: LongArray) =
        getOrCreateHeightmap(type).setData(this, type, data)

    override fun getNoiseBiome(x: Int, y: Int, z: Int): Biome {
        val minQuart = Quart.fromBlock(minimumBuildHeight)
        val maxQuart = minQuart + Quart.fromBlock(height) - 1
        val clamped = y.clamp(minQuart, maxQuart)
        val index = sectionIndex(Quart.toBlock(clamped))
        return sections[index]!!.getNoiseBiome(x and 3, clamped and 3, z and 3)
    }
}
