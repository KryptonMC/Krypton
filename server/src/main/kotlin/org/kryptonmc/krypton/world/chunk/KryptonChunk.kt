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

import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.packet.CachedPacket
import org.kryptonmc.krypton.packet.out.play.PacketOutChunkDataAndLight
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.coordinate.QuartPos
import org.kryptonmc.krypton.ticking.Tickable
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.world.chunk.data.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.biome.NoiseBiomeSource
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.entity.KryptonBlockEntity
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.chunk.data.ChunkSection
import org.kryptonmc.krypton.world.components.BlockGetter
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import org.kryptonmc.krypton.world.fluid.KryptonFluids
import java.util.EnumMap

@Suppress("INAPPLICABLE_JVM_NAME")
class KryptonChunk(
    override val world: KryptonWorld,
    override val position: ChunkPos,
    private val sections: Array<ChunkSection>,
) : BaseChunk, BlockGetter, NoiseBiomeSource, Tickable {

    override var lastUpdate: Long = 0L
    override var inhabitedTime: Long = 0L

    private val heightmaps = EnumMap<_, Heightmap>(Heightmap.Type::class.java)
    val cachedPacket: CachedPacket = CachedPacket { PacketOutChunkDataAndLight.fromChunk(this, true) }

    override fun getBlock(x: Int, y: Int, z: Int): KryptonBlockState {
        val sectionIndex = getSectionIndex(y)
        if (sectionIndex >= 0 && sectionIndex < sections.size) {
            val section = sections[sectionIndex]
            if (!section.hasOnlyAir()) return section.getBlock(x and 15, y and 15, z and 15)
        }
        return KryptonBlocks.AIR.defaultState
    }

    override fun getFluid(x: Int, y: Int, z: Int): KryptonFluidState {
        val sectionIndex = getSectionIndex(y)
        if (sectionIndex >= 0 && sectionIndex < sections.size) {
            val section = sections[sectionIndex]
            if (!section.hasOnlyAir()) return section.getBlock(x and 15, y and 15, z and 15).asFluid()
        }
        return KryptonFluids.EMPTY.defaultState
    }

    @Suppress("UnusedPrivateMember")
    fun setBlock(pos: Vec3i, state: KryptonBlockState, moving: Boolean): KryptonBlockState? {
        val section = sections[getSectionIndex(pos.y)]
        if (section.hasOnlyAir() && state.isAir()) return null

        // Get the local coordinates and set the new state in the section
        val localX = pos.x and 15
        val localY = pos.y and 15
        val localZ = pos.z and 15
        val oldState = section.setBlock(localX, localY, localZ, state)
        if (oldState === state) return null

        // Update the heightmaps
        heightmaps.getValue(Heightmap.Type.MOTION_BLOCKING).update(localX, pos.y, localZ, state)
        heightmaps.getValue(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES).update(localX, pos.y, localZ, state)
        heightmaps.getValue(Heightmap.Type.OCEAN_FLOOR).update(localX, pos.y, localZ, state)
        heightmaps.getValue(Heightmap.Type.WORLD_SURFACE).update(localX, pos.y, localZ, state)
        cachedPacket.invalidate()
        return oldState
    }

    override fun getBlockEntity(x: Int, y: Int, z: Int): KryptonBlockEntity? = null // TODO: Implement

    override fun getNoiseBiome(x: Int, y: Int, z: Int): Biome {
        val minimumQuart = QuartPos.fromBlock(minimumBuildHeight())
        val maximumQuart = minimumQuart + QuartPos.fromBlock(height()) - 1
        val actualY = Maths.clamp(y, minimumQuart, maximumQuart)
        val sectionIndex = getSectionIndex(QuartPos.toBlock(actualY))
        return sections[sectionIndex].getNoiseBiome(x and 3, actualY and 3, z and 3)
    }

    override fun tick(time: Long) {
        // Nothing we really want to do here yet
    }

    fun sections(): Array<ChunkSection> = sections

    private fun highestNonEmptySectionIndex(): Int {
        for (i in sections.size - 1 downTo 0) {
            val section = sections[i]
            if (!section.hasOnlyAir()) return i
        }
        return -1
    }

    fun highestSectionY(): Int {
        val highestSectionIndex = highestNonEmptySectionIndex()
        if (highestSectionIndex == -1) return minimumBuildHeight()
        return getSectionYFromSectionIndex(highestSectionIndex)
    }

    fun heightmaps(): Map<Heightmap.Type, Heightmap> = heightmaps

    fun getOrCreateHeightmap(type: Heightmap.Type): Heightmap = heightmaps.computeIfAbsent(type) { Heightmap(this, type) }

    fun setHeightmap(type: Heightmap.Type, data: LongArray) {
        getOrCreateHeightmap(type).setData(this, type, data)
    }
}
