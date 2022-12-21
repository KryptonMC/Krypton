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

import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.chunk.Chunk
import org.kryptonmc.krypton.packet.CachedPacket
import org.kryptonmc.krypton.packet.out.play.PacketOutChunkDataAndLight
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.entity.KryptonBlockEntity
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.chunk.ticket.Ticket
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import org.kryptonmc.krypton.world.fluid.KryptonFluids
import org.kryptonmc.nbt.CompoundTag

@Suppress("INAPPLICABLE_JVM_NAME")
class KryptonChunk(
    override val world: KryptonWorld,
    position: ChunkPos,
    sections: Array<ChunkSection?>,
    override var lastUpdate: Long,
    inhabitedTime: Long,
    val ticket: Ticket<*>,
    val carvingMasks: Pair<ByteArray, ByteArray>,
    val structures: CompoundTag
) : ChunkAccessor(position, world, inhabitedTime, sections), Chunk {

    override val status: ChunkStatus = ChunkStatus.FULL

    override val x: Int
        get() = position.x
    override val z: Int
        get() = position.z

    val cachedPacket: CachedPacket = CachedPacket { PacketOutChunkDataAndLight(this, true) }

    override fun getBlock(x: Int, y: Int, z: Int): KryptonBlockState {
//        if (world.isDebug) {
//            var block: KryptonBlock? = null
//            if (y == 60) block = KryptonBlocks.BARRIER.defaultState
//            if (y == 70) block = DebugGenerator.blockAt(x, z)
//            return block ?: KryptonBlocks.AIR.defaultState
//        }
        val sectionIndex = getSectionIndex(y)
        if (sectionIndex >= 0 && sectionIndex < sections().size) {
            val section = sections()[sectionIndex]
            if (!section.hasOnlyAir()) return section.getBlock(x and 15, y and 15, z and 15)
        }
        return KryptonBlocks.AIR.defaultState
    }

    override fun getBlock(position: Vec3i): KryptonBlockState = getBlock(position.x, position.y, position.z)

    override fun getFluid(x: Int, y: Int, z: Int): KryptonFluidState {
        val sectionIndex = getSectionIndex(y)
        if (sectionIndex >= 0 && sectionIndex < sections().size) {
            val section = sections()[sectionIndex]
            if (!section.hasOnlyAir()) return section.getBlock(x and 15, y and 15, z and 15).asFluid()
        }
        return KryptonFluids.EMPTY.defaultState
    }

    override fun getFluid(position: Vec3i): KryptonFluidState = getFluid(position.x, position.y, position.z)

    override fun setBlock(pos: BlockPos, state: KryptonBlockState, moving: Boolean): KryptonBlockState? {
        val section = sections()[getSectionIndex(pos.y)]
        if (section.hasOnlyAir() && state.isAir) return null

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

    override fun getBiome(x: Int, y: Int, z: Int): Biome = getNoiseBiome(x, y, z)

    override fun getBiome(position: Vec3i): Biome = getBiome(position.x, position.y, position.z)

    fun tick(playerCount: Int) {
        inhabitedTime += playerCount
    }

    private fun lightSectionCount(): Int = sectionCount() + 2

    fun minimumLightSection(): Int = minimumSection() - 1

    fun maximumLightSection(): Int = minimumLightSection() + lightSectionCount()
}
