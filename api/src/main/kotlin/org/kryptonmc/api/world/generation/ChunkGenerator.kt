package org.kryptonmc.api.world.generation

import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.biome.BiomeProvider
import org.kryptonmc.api.world.chunk.Chunk
import java.util.concurrent.CompletableFuture

/**
 * The generator that handles generation of chunks.
 */
public interface ChunkGenerator {

    public val world: World

    public val pipeline: GenerationPipeline

    public val biomeProvider: BiomeProvider

    /**
     * Creates a new task that can be started, stopped, and advanced, for finer grained control over
     * the generation process.
     */
    public fun createGenerationTask(x: Int, y: Int): GenerationTask

    /**
     * Allows to just avoid using the generation task entirely and easily generate a chunk fully.
     *
     * Should return an existing chunk if already generated.
     */
    public fun generateChunk(x: Int, z: Int): CompletableFuture<Chunk>
}
