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

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.fluid.Fluids
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.chunk.Chunk
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.ticket.Ticket
import org.kryptonmc.krypton.world.generation.DebugGenerator
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3i

@Suppress("INAPPLICABLE_JVM_NAME")
class KryptonChunk(
    override val world: KryptonWorld,
    position: ChunkPosition,
    sections: Array<ChunkSection?>,
    override var lastUpdate: Long,
    inhabitedTime: Long,
    val ticket: Ticket<*>,
    val carvingMasks: Pair<ByteArray, ByteArray>,
    val structures: CompoundTag
) : ChunkAccessor(position, world, inhabitedTime, sections), Chunk {

    override val status: ChunkStatus = ChunkStatus.FULL
    override val height = world.height
    override val minimumBuildHeight = world.minimumBuildHeight

    private val lightSectionCount = sectionCount + 2
    val minimumLightSection = minimumSection - 1
    val maximumLightSection = minimumLightSection + lightSectionCount

    override val x: Int
        get() = position.x
    override val z: Int
        get() = position.z

    override fun getBlock(x: Int, y: Int, z: Int): Block {
        if (world.isDebug) {
            var block: Block? = null
            if (y == 60) block = Blocks.BARRIER
            if (y == 70) block = DebugGenerator.blockAt(x, z)
            return block ?: Blocks.AIR
        }
        val sectionIndex = sectionIndex(y)
        if (sectionIndex >= 0 && sectionIndex < sections.size) {
            val section = sections[sectionIndex]
            if (!section.hasOnlyAir()) return section[x and 15, y and 15, z and 15]
        }
        return Blocks.AIR
    }

    override fun getBlock(position: Vector3i): Block = getBlock(position.x(), position.y(), position.z())

    override fun getFluid(x: Int, y: Int, z: Int): Fluid {
        val sectionIndex = sectionIndex(y)
        if (sectionIndex >= 0 && sectionIndex < sections.size) {
            val section = sections[sectionIndex]
            if (!section.hasOnlyAir()) return section[x and 15, y and 15, z and 15].asFluid()
        }
        return Fluids.EMPTY
    }

    override fun getFluid(position: Vector3i): Fluid = getFluid(position.x(), position.y(), position.z())

    override fun setBlock(x: Int, y: Int, z: Int, block: Block): Boolean {
        val section = sections[sectionIndex(y)]
        if (section.hasOnlyAir() && block.isAir) return false

        // Get the local coordinates and set the new state in the section
        val localX = x and 15
        val localY = y and 15
        val localZ = z and 15
        val oldState = section.set(localX, localY, localZ, block)
        if (oldState === block) return false

        // Update the heightmaps
        heightmaps.getValue(Heightmap.Type.MOTION_BLOCKING).update(localX, y, localZ, block)
        heightmaps.getValue(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES).update(localX, y, localZ, block)
        heightmaps.getValue(Heightmap.Type.OCEAN_FLOOR).update(localX, y, localZ, block)
        heightmaps.getValue(Heightmap.Type.WORLD_SURFACE).update(localX, y, localZ, block)
        return true
    }

    override fun setBlock(position: Vector3i, block: Block): Boolean = setBlock(position.x(), position.y(), position.z(), block)

    override fun getBiome(x: Int, y: Int, z: Int): Biome = getNoiseBiome(x, y, z)

    override fun getBiome(position: Vector3i): Biome = getBiome(position.x(), position.y(), position.z())

    fun tick(playerCount: Int) {
        inhabitedTime += playerCount
    }
}
