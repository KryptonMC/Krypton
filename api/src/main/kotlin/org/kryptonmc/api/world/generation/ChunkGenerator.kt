package org.kryptonmc.api.world.generation

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide
import org.kryptonmc.api.world.biome.BiomeGenerator
import org.kryptonmc.api.world.chunk.GenerationChunk

/**
 * A generator for generating chunks.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ChunkGenerator {

    @get:JvmName("biomeGenerator")
    public val biomeGenerator: BiomeGenerator

    @get:JvmName("stages")
    public val stages: Set<GenerationStage>

    public fun generate(chunk: GenerationChunk)

    @ApiStatus.Internal
    public interface Factory {

        public fun debug(): ChunkGenerator
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        @JvmStatic
        public fun debug(): ChunkGenerator = FACTORY.debug()
    }
}
