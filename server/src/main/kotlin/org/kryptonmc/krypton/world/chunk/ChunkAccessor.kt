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
package org.kryptonmc.krypton.world.chunk

import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.coordinate.QuartPos
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.data.Heightmap
import org.kryptonmc.krypton.world.biome.NoiseBiomeSource
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.chunk.data.ChunkSection
import org.kryptonmc.krypton.world.chunk.data.ChunkStatus
import org.kryptonmc.krypton.world.components.BlockGetter
import java.util.EnumMap
import java.util.EnumSet

abstract class ChunkAccessor(
    val position: ChunkPos,
    private val world: KryptonWorld,
    var inhabitedTime: Long,
    sections: Array<ChunkSection?>?
) : BlockGetter, NoiseBiomeSource {

    abstract val status: ChunkStatus

    private val sections = arrayOfNulls<ChunkSection>(world.sectionCount())
    val heightmaps: MutableMap<Heightmap.Type, Heightmap> = EnumMap(Heightmap.Type::class.java)

    init {
        if (sections != null) {
            if (this.sections.size == sections.size) {
                System.arraycopy(sections, 0, this.sections, 0, this.sections.size)
            } else {
                LOGGER.warn("Failed to set chunk sections! Expected array size ${this.sections.size} but got ${sections.size}!")
            }
        }
        replaceMissingSections(world, this.sections)
    }

    override fun height(): Int = world.height()

    override fun minimumBuildHeight(): Int = world.minimumBuildHeight()

    @Suppress("UNCHECKED_CAST")
    fun sections(): Array<ChunkSection> = sections as Array<ChunkSection>

    fun highestSectionY(): Int = highestSection()?.bottomBlockY ?: minimumBuildHeight()

    abstract fun setBlock(pos: Vec3i, state: KryptonBlockState, moving: Boolean): KryptonBlockState?

    private fun getSection(index: Int): ChunkSection = sections[index]!!

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
        val minimumQuart = QuartPos.fromBlock(minimumBuildHeight())
        val maximumQuart = minimumQuart + QuartPos.fromBlock(height()) - 1
        val actualY = Maths.clamp(y, minimumQuart, maximumQuart)
        val sectionIndex = getSectionIndex(QuartPos.toBlock(actualY))
        return getSection(sectionIndex).getNoiseBiome(x and 3, actualY and 3, z and 3)
    }

    private fun highestSection(): ChunkSection? {
        for (i in sections.size - 1 downTo 0) {
            val section = sections[i]
            if (!section!!.hasOnlyAir()) return section
        }
        return null
    }

    companion object {

        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        private fun replaceMissingSections(world: KryptonWorld, sections: Array<ChunkSection?>) {
            val biomeRegistry = world.registryHolder.getRegistry(ResourceKeys.BIOME) as KryptonRegistry<Biome>
            for (i in sections.indices) {
                if (sections[i] == null) sections[i] = ChunkSection(world.getSectionYFromSectionIndex(i), biomeRegistry)
            }
        }
    }
}
