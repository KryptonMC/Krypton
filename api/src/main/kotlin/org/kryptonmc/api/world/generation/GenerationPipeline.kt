package org.kryptonmc.api.world.generation

/**
 * A pipeline that specifies the order in which generation stages will run.
 */
public interface GenerationPipeline {

    public fun addStage(stage: GenerationStage)

    public fun insertStage(index: Int, stage: GenerationStage)

    public fun removeStage(stage: GenerationStage)

    public fun removeStage(index: Int)
}
