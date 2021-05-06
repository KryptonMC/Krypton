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

import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import org.kryptonmc.krypton.api.block.Block
import org.kryptonmc.krypton.api.inventory.item.Material
import org.kryptonmc.krypton.api.space.Position
import org.kryptonmc.krypton.api.world.Biome
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World
import org.kryptonmc.krypton.api.world.chunk.Chunk
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.HeightmapBuilder
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.data.BitStorage

data class KryptonChunk(
    override val world: World,
    val position: ChunkPosition,
    val sections: MutableList<ChunkSection>,
    override val biomes: List<Biome>,
    override var lastUpdate: Long,
    override var inhabitedTime: Long,
    private val builderHeightmaps: List<HeightmapBuilder>,
    val carvingMasks: Pair<ByteArray, ByteArray>,
    val structures: CompoundBinaryTag
) : Chunk {

    val heightmaps = builderHeightmaps.map { Heightmap(this, it.nbt, it.type) }.associateBy { it.type }

    override val x: Int get() = position.x
    override val z: Int get() = position.z

    override fun getBlock(position: Position): KryptonBlock {
        val x = position.blockX and 0xF
        val y = position.blockY
        val z = position.blockZ and 0xF
        val section = sections.firstOrNull { it.y == y shr 4 }
            ?: return KryptonBlock(Material.AIR, this, Location(world, position.x, position.y, position.z))

        val name = section[x, y and 0xF, z]
        return KryptonBlock(Material.KEYS.value(name)!!, this, Location(world, position.x, position.y, position.z))
    }

    override fun setBlock(block: Block): Boolean {
        if (block !is KryptonBlock) return false

        val x = block.location.blockX and 0xF
        val y = block.location.blockY
        val z = block.location.blockZ and 0xF

        val sectionY = y shr 4
        if (sectionY !in 0..15) {
            LOGGER.warn("Attempted to place block $block under/over the world! The section at $sectionY cannot be set!")
            return false
        }

        val section = sections.firstOrNull { it.y == sectionY } ?: ChunkSection(sectionY).apply {
            sections += this
            palette += ChunkBlock.AIR
            palette += ChunkBlock(block.type.key)
        }
        if (ChunkBlock.AIR !in section.palette) section.palette.addFirst(ChunkBlock.AIR)
        if (section.blockStates.data.isEmpty()) section.blockStates = BitStorage(4, 4096)
        if (!section.set(block)) return false

        heightmaps.getValue(Heightmap.Type.MOTION_BLOCKING).update(x, y, z, block.type)
        heightmaps.getValue(Heightmap.Type.OCEAN_FLOOR).update(x, y, z, block.type)
        heightmaps.getValue(Heightmap.Type.WORLD_SURFACE).update(x, y, z, block.type)
        return true
    }

    fun tick(playerCount: Int) {
        inhabitedTime += playerCount
    }

    companion object {

        private val LOGGER = logger<KryptonChunk>()
    }
}

data class Heightmaps(
    val motionBlocking: LongArrayBinaryTag,
    val oceanFloor: LongArrayBinaryTag,
    val worldSurface: LongArrayBinaryTag,
)

// TODO: Do things with these
//data class TileTick(
//    val block: Block,
//    val priority: Int,
//    val delay: Int
//)
//
//data class StructureData(
//    val references: Map<String, Vector>,
//    val starts: List<Structure>
//)
