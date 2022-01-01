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
import org.kryptonmc.api.block.entity.BlockEntity
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.fluid.Fluids
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.chunk.Chunk
import org.kryptonmc.krypton.packet.CachedPacket
import org.kryptonmc.krypton.packet.out.play.PacketOutChunkDataAndLight
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.entity.BlockEntityFactory
import org.kryptonmc.krypton.world.block.entity.blockEntityType
import org.kryptonmc.krypton.world.block.handler
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
    override val height: Int = world.height
    override val minimumBuildHeight: Int = world.minimumBuildHeight

    private val lightSectionCount = sectionCount + 2
    val minimumLightSection: Int = minimumSection - 1
    val maximumLightSection: Int = minimumLightSection + lightSectionCount

    override val x: Int
        get() = position.x
    override val z: Int
        get() = position.z

    val cachedPacket: CachedPacket = CachedPacket { PacketOutChunkDataAndLight(this, true) }

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

    @Suppress("UNCHECKED_CAST")
    override fun <T : BlockEntity> getBlockEntity(x: Int, y: Int, z: Int): T? {
        var entity = blockEntities[encodePosition(x, y, z)]
        if (entity == null) {
            val block = getBlock(x, y, z)
            val type = block.blockEntityType() ?: return null
            entity = BlockEntityFactory.create(type, world, block, Vector3i(x, y, z))
            blockEntities[encodePosition(x, y, z)] = entity
        }
        return entity as T
    }

    override fun getFluid(x: Int, y: Int, z: Int): Fluid {
        val sectionIndex = sectionIndex(y)
        if (sectionIndex >= 0 && sectionIndex < sections.size) {
            val section = sections[sectionIndex]
            if (!section.hasOnlyAir()) return section[x and 15, y and 15, z and 15].asFluid()
        }
        return Fluids.EMPTY
    }

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

        oldState.handler().onRemove(oldState, block, world, x, y, z, false)
        if (section[x, y, z] !== block) return true
        block.handler().onPlace(oldState, block, world, x, y, z, false)

        if (block.hasBlockEntity) {
            val encodedPosition = encodePosition(x, y, z)
            var blockEntity = blockEntities[encodedPosition]
            if (blockEntity == null) {
                val type = block.blockEntityType() ?: return true
                blockEntity = BlockEntityFactory.create(type, world, block, Vector3i(x, y, z))
                blockEntities[encodedPosition] = blockEntity
            } else {
                blockEntity.block = block
            }
        }
        cachedPacket.invalidate()
        return true
    }

    override fun removeBlockEntity(x: Int, y: Int, z: Int) {
        blockEntities.remove(encodePosition(x, y, z))
    }

    override fun getBiome(x: Int, y: Int, z: Int): Biome = getNoiseBiome(x, y, z)

    override fun getBiome(position: Vector3i): Biome = getBiome(position.x(), position.y(), position.z())

    fun tick(playerCount: Int) {
        inhabitedTime += playerCount
    }
}
