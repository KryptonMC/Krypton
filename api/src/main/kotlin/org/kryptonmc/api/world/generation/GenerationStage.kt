package org.kryptonmc.api.world.generation

import org.kryptonmc.api.world.chunk.ProtoChunk
import org.kryptonmc.api.world.generation.context.GenerationContext

/**
 * A stage in the generation process. These do the actual generation work, and are designed
 * to be modular and reusable.
 */
public interface GenerationStage {

    public val name: String

    /**
     * Runs the generation work for this stage on the given chunk.
     *
     * The context is provided to allow the pipeline to pass information to stages (global context),
     * as well as for stages to provide information to each other (local context).
     */
    public fun generate(chunk: ProtoChunk, context: GenerationContext)
}
