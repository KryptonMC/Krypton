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
import org.kryptonmc.krypton.world.HeightAccessor
import org.kryptonmc.krypton.world.Heightmap
import org.spongepowered.math.vector.Vector3i
import java.util.EnumMap
import java.util.EnumSet

class ProtoChunk(
    override val position: ChunkPosition,
    override val sections: Array<ChunkSection?>,
    private val heightAccessor: HeightAccessor
) : ChunkAccessor, HeightAccessor by heightAccessor {

    private val lights = mutableListOf<Vector3i>()

    override var inhabitedTime = 0L
    override var isLightCorrect = false
    override var isUnsaved = false
    override var status = ChunkStatus.EMPTY
    override var biomes: KryptonBiomeContainer? = null
    override val heightmaps = EnumMap<Heightmap.Type, Heightmap>(Heightmap.Type::class.java)

    override fun getBlock(x: Int, y: Int, z: Int): Block {
        if (y.outsideBuildHeight) return Blocks.VOID_AIR
        val section = sections[sectionIndex(y)]
        return if (section == null || section.isEmpty()) Blocks.AIR else section[x and 15, y and 15, z and 15]
    }

    override fun getFluid(x: Int, y: Int, z: Int): Fluid {
        if (y.outsideBuildHeight) return Fluids.EMPTY
        val section = sections[sectionIndex(y)]
        return if (section == null || section.isEmpty()) Fluids.EMPTY else section[x and 15, y and 15, z and 15].asFluid()
    }

    override fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        if (y !in minimumBuildHeight..maximumBuildHeight) return // Out of bounds

        // Get and check section and block
        val sectionIndex = sectionIndex(y)
        if (sections[sectionIndex] == null && block === Blocks.AIR) return

        // Add a new light source if the block emits light
        if (block.lightEmission > 0) lights.add(Vector3i((x and 15) + (position.x shl 4), y, (z and 15) + (position.z shl 4)))

        // Get or create the section and set the block
        val section = getOrCreateSection(sectionIndex)
        val oldBlock = section.set(x and 15, y and 15, z and 15, block)
        if (status.isOrAfter(ChunkStatus.FEATURES) && block != oldBlock && (block.lightBlock != oldBlock.lightBlock || block.lightEmission != oldBlock.lightEmission || block.useShapeForOcclusion != oldBlock.useShapeForOcclusion)) {
            // TODO: Inform light engine to check block
        }

        // Prime and update the heightmaps
        val typesAfter = status.heightmapsAfter
        var types: EnumSet<Heightmap.Type>? = null
        typesAfter.forEach {
            if (heightmaps[it] != null) return@forEach
            if (types == null) types = EnumSet.noneOf(Heightmap.Type::class.java)
            types!!.add(it)
        }
        types?.let { Heightmap.prime(this, it) }
        typesAfter.forEach { heightmaps[it]?.update(x and 15, y, z and 15, block) }
    }

    override fun getOrCreateHeightmap(type: Heightmap.Type): Heightmap = heightmaps.getOrPut(type) { Heightmap(this, type) }

    override fun getHeight(type: Heightmap.Type, x: Int, z: Int): Int {
        var heightmap = heightmaps[type]
        if (heightmap == null) {
            Heightmap.prime(this, EnumSet.of(type))
            heightmap = heightmaps[type]!!
        }
        return heightmap.firstAvailable(x and 15, z and 15) - 1
    }
}
