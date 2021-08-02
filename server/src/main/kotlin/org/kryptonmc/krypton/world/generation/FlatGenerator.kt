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

import com.mojang.serialization.Codec
import org.kryptonmc.krypton.space.MutableVector3i
import org.kryptonmc.krypton.world.HeightAccessor
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.StructureFeatureManager
import org.kryptonmc.krypton.world.biome.KryptonBiomes
import org.kryptonmc.krypton.world.biome.gen.FixedBiomeGenerator
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import org.kryptonmc.krypton.world.generation.flat.FlatGeneratorSettings
import org.kryptonmc.krypton.world.generation.noise.NoiseColumn
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.math.min

// FIXME: Get the biomes from the settings
class FlatGenerator(val settings: FlatGeneratorSettings) : Generator(
    FixedBiomeGenerator(KryptonBiomes.PLAINS),
    FixedBiomeGenerator(settings.biome),
    settings.structureSettings
) {

    override val codec = CODEC

    override fun buildSurface(region: GenerationRegion, chunk: ChunkAccessor) = Unit

    override fun fillFromNoise(executor: Executor, structureFeatureManager: StructureFeatureManager, chunk: ChunkAccessor): CompletableFuture<ChunkAccessor> {
        val layers = settings.blockLayers
        val pos = MutableVector3i()
        val oceanFloor = chunk.getOrCreateHeightmap(Heightmap.Type.OCEAN_FLOOR_WG)
        val worldSurface = chunk.getOrCreateHeightmap(Heightmap.Type.WORLD_SURFACE_WG)
        for (i in 0 until min(chunk.height, layers.size)) {
            val layer = layers[i]
            val y = chunk.minimumBuildHeight + i
            for (x in 0 until 16) {
                for (z in 0 until 16) {
                    pos.set(x, y, z)
                    chunk.setBlock(x, y, z, layer)
                    oceanFloor.update(x, y, z, layer)
                    worldSurface.update(x, y, z, layer)
                }
            }
        }
        return CompletableFuture.completedFuture(chunk)
    }

    override fun getBaseHeight(x: Int, z: Int, type: Heightmap.Type, heightAccessor: HeightAccessor): Int {
        val layers = settings.blockLayers
        for (i in min(layers.size, heightAccessor.maximumBuildHeight) - 1 downTo 0) {
            val block = layers[i]
            if (type.isOpaque(block)) return heightAccessor.minimumBuildHeight + i + 1
        }
        return heightAccessor.minimumBuildHeight
    }

    override fun getBaseColumn(x: Int, z: Int, heightAccessor: HeightAccessor) = NoiseColumn(
        heightAccessor.minimumBuildHeight,
        settings.blockLayers.asSequence().take(heightAccessor.height).toList().toTypedArray()
    )

    override fun getSpawnHeight(heightAccessor: HeightAccessor) = heightAccessor.minimumBuildHeight + min(heightAccessor.height, settings.layers.size)

    companion object {

        val CODEC: Codec<FlatGenerator> = FlatGeneratorSettings.CODEC.fieldOf("settings").xmap(::FlatGenerator, FlatGenerator::settings).codec()
    }
}
