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
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.api.block.Block
import org.kryptonmc.krypton.space.MutableVector3i
import org.kryptonmc.krypton.util.noise.BlendedNoise
import org.kryptonmc.krypton.util.noise.NormalNoise
import org.kryptonmc.krypton.util.noise.PerlinNoise
import org.kryptonmc.krypton.util.noise.PerlinSimplexNoise
import org.kryptonmc.krypton.util.noise.SimplexNoise
import org.kryptonmc.krypton.util.noise.SurfaceNoise
import org.kryptonmc.krypton.util.random.SimpleRandomSource
import org.kryptonmc.krypton.util.random.WorldGenRandom
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.biome.gen.BiomeGenerator
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import org.kryptonmc.krypton.world.generation.noise.NoiseGeneratorSettings
import org.kryptonmc.krypton.world.generation.noise.NoiseModifier

class NoiseGenerator(
    biomeGenerator: BiomeGenerator,
    private val seed: Long,
    private val settings: NoiseGeneratorSettings
) : Generator(biomeGenerator, biomeGenerator, settings.structureSettings, seed) {

    private val cellHeight: Int
    private val cellWidth: Int
    private val cellCountX: Int
    private val cellCountY: Int
    private val cellCountZ: Int
    private val surfaceNoise: SurfaceNoise
    private val barrierNoise: NormalNoise
    private val waterLevelNoise: NormalNoise
    private val lavaNoise: NormalNoise
    private val defaultBlock: Block
    private val defaultFluid: Block
    private val height: Int
    private val sampler: NoiseSampler
    override val codec = CODEC

    init {
        val noiseSettings = settings.noiseSettings
        height = noiseSettings.height
        cellHeight = noiseSettings.verticalSize shl 2
        cellWidth = noiseSettings.horizontalSize shl 2
        defaultBlock = settings.defaultBlock
        defaultFluid = settings.defaultFluid
        cellCountX = 16 / cellWidth
        cellCountY = noiseSettings.height / cellHeight
        cellCountZ = 16 / cellWidth
        val random = WorldGenRandom(seed)
        val blendedNoise = BlendedNoise(random)
        surfaceNoise = if (noiseSettings.useSimplexSurfaceNoise) PerlinSimplexNoise(random, -3..0) else PerlinNoise(random, -3..0)
        random.skip(2620)
        val depthNoise = PerlinNoise(random, -15..0)
        val islandNoise = if (noiseSettings.islandNoiseOverride) {
            val islandRandom = WorldGenRandom(seed).apply { skip(17292) }
            SimplexNoise(islandRandom)
        } else null
        barrierNoise = NormalNoise(SimpleRandomSource(random.nextLong()), -3, 1.0)
        waterLevelNoise = NormalNoise(SimpleRandomSource(random.nextLong()), -3, 1.0, 0.0, 2.0)
        lavaNoise = NormalNoise(SimpleRandomSource(random.nextLong()), -1, 1.0, 0.0)
        val modifier = NoiseModifier.PASSTHROUGH // TODO: Check for noise caves and set to cavifier
        sampler = NoiseSampler(biomeGenerator, cellWidth, cellHeight, cellCountY, noiseSettings, blendedNoise, islandNoise, depthNoise, modifier)
    }

    override fun buildSurface(region: GenerationRegion, chunk: ChunkAccessor) {
        val x = chunk.position.x
        val z = chunk.position.z
        val random = WorldGenRandom().apply { setBaseChunkSeed(x, z) }
        val minBlockX = x shl 4
        val minBlockZ = z shl 4
        val pos = MutableVector3i()
        for (xo in 0..15) {
            for (zo in 0..15) {
                val xOff = minBlockX + xo
                val zOff = minBlockZ + zo
                val height = chunk.getHeight(Heightmap.Type.WORLD_SURFACE_WG, xo, zo) + 1
                val noise = surfaceNoise.getValue(xOff.toDouble() * 0.0625, zOff.toDouble() * 0.0625, 0.0625, xo.toDouble() * 0.0625) * 15.0
                val minSurfaceLevel = settings.minimumSurfaceLevel
                // TODO: Get biomes from region and build surface
            }
        }
        // TODO: Set bedrock
    }

    companion object {

        val CODEC: Codec<NoiseGenerator> = RecordCodecBuilder.create { instance ->
            instance.group(
                BiomeGenerator.CODEC.fieldOf("biome_source").forGetter { it.biomeGenerator },
                Codec.LONG.fieldOf("seed").stable().forGetter(NoiseGenerator::seed),
                NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(NoiseGenerator::settings)
            ).apply(instance, ::NoiseGenerator)
        }
    }
}
