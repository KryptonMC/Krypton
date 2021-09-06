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
import org.kryptonmc.api.world.chunk.Chunk
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.ticket.Ticket
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3i
import java.util.EnumMap

class KryptonChunk(
    override val world: KryptonWorld,
    override val position: ChunkPosition,
    override val sections: Array<ChunkSection?>,
    override val biomes: KryptonBiomeContainer,
    override var lastUpdate: Long,
    override var inhabitedTime: Long,
    val ticket: Ticket<*>,
    val carvingMasks: Pair<ByteArray, ByteArray>,
    val structures: CompoundTag
) : Chunk, ChunkAccessor {

    override val heightmaps = EnumMap<Heightmap.Type, Heightmap>(Heightmap.Type::class.java)

    override val status = ChunkStatus.FULL
    override var isLightCorrect = false
    override var isUnsaved = false
    override val height = world.height
    override val minimumBuildHeight = world.minimumBuildHeight

    val lightSectionCount = sectionCount + 2
    val minimumLightSection = minimumSection - 1
    val maximumLightSection = minimumLightSection + lightSectionCount

    override val x = position.x
    override val z = position.z

    override fun getBlock(x: Int, y: Int, z: Int): Block {
        val sectionIndex = sectionIndex(y)
        if (sectionIndex in sections.indices) {
            val section = sections[sectionIndex]
            if (section != null && !section.isEmpty()) return section[x and 15, y and 15, z and 15]
        }
        return Blocks.AIR
    }

    override fun getBlock(position: Vector3i) = getBlock(position.x(), position.y(), position.z())

    override fun getFluid(x: Int, y: Int, z: Int): Fluid {
        val sectionIndex = sectionIndex(y)
        if (sectionIndex in sections.indices) {
            val section = sections[sectionIndex]
            if (section != null && !section.isEmpty()) return section[x and 15, y and 15, z and 15].asFluid()
        }
        return Fluids.EMPTY
    }

    override fun getFluid(position: Vector3i) = getFluid(position.x(), position.y(), position.z())

    override fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        // Get the section
        val sectionIndex = sectionIndex(y)
        var section = sections[sectionIndex]
        if (section == null) {
            if (block.isAir) return
            section = ChunkSection(y shr 4)
            sections[sectionIndex] = section
        }

        // Get the local coordinates and set the new state in the section
        val localX = x and 15
        val localY = y and 15
        val localZ = z and 15
        val oldState = section.set(localX, localY, localZ, block)
        if (oldState == block) return

        // Update the heightmaps
        heightmaps.getValue(Heightmap.Type.MOTION_BLOCKING).update(localX, y, localZ, block)
        heightmaps.getValue(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES).update(localX, y, localZ, block)
        heightmaps.getValue(Heightmap.Type.OCEAN_FLOOR).update(localX, y, localZ, block)
        heightmaps.getValue(Heightmap.Type.WORLD_SURFACE).update(localX, y, localZ, block)
        return
    }

    override fun setBlock(position: Vector3i, block: Block) = setBlock(position.x(), position.y(), position.z(), block)

    fun tick(playerCount: Int) {
        inhabitedTime += playerCount
    }

    override fun getOrCreateHeightmap(type: Heightmap.Type): Heightmap =
        heightmaps.getOrPut(type) { Heightmap(this, type) }

    override fun getHeight(type: Heightmap.Type, x: Int, z: Int) =
        heightmaps[type]!!.firstAvailable(x and 15, z and 15) - 1

    override fun setHeightmap(type: Heightmap.Type, data: LongArray) =
        heightmaps.getOrPut(type) { Heightmap(this, type) }.setData(this, type, data)
}
