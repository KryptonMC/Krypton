package org.kryptonmc.krypton.world.generation

import org.kryptonmc.api.world.biome.BiomeGenerator
import org.kryptonmc.api.world.chunk.GenerationChunk
import org.kryptonmc.api.world.generation.ChunkGenerator
import org.kryptonmc.api.world.generation.GenerationStage

abstract class KryptonChunkGenerator(
    override val biomeGenerator: BiomeGenerator,
    override val stages: Set<GenerationStage>
) : ChunkGenerator {

    override fun generate(chunk: GenerationChunk) {
        stages.forEach { it.apply(chunk) }
    }
}
