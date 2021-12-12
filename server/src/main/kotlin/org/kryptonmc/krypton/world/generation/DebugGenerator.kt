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
package org.kryptonmc.krypton.world.generation

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.util.ceil
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.biome.Climate
import org.kryptonmc.krypton.world.biome.gen.FixedBiomeGenerator
import org.kryptonmc.krypton.world.block.BlockLoader
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import org.spongepowered.math.GenericMath
import kotlin.math.abs

class DebugGenerator(biomes: Registry<Biome>) : Generator(FixedBiomeGenerator(biomes[BiomeKeys.PLAINS]!!), StructureSettings(false)) {

    override val climateSampler: Climate.Sampler = Climate.Sampler { _, _, _ -> Climate.TargetPoint.ZERO }

    override fun buildSurface(region: GenerationRegion, chunk: ChunkAccessor) {
        // no surface to build for debug generator
    }

    companion object {

        private val GRID_WIDTH = GenericMath.sqrt(BlockLoader.STATES.size.toDouble()).ceil()
        private val GRID_HEIGHT = (BlockLoader.STATES.size.toDouble() / GRID_WIDTH).ceil()

        @JvmStatic
        fun blockAt(x: Int, z: Int): Block {
            var block = Blocks.AIR
            if (x > 0 && z > 0 && x % 2 != 0 && z % 2 != 0) {
                val newX = x / 2
                val newZ = z / 2
                if (newX <= GRID_WIDTH && newZ <= GRID_HEIGHT) {
                    val index = abs(newX * GRID_WIDTH + newZ)
                    if (index < BlockLoader.STATES.size) block = BlockLoader.STATES[index]!!
                }
            }
            return block
        }
    }
}
