package org.kryptonmc.api.world.generation

import org.kryptonmc.api.world.chunk.GenerationChunk

public interface GenerationStage {

    public fun apply(chunk: GenerationChunk)
}
