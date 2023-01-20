package org.kryptonmc.api.world.generation

/**
 * Used to allow finer grained control over the generation process.
 *
 * Represents a single task to generate a single chunk.
 */
public interface GenerationTask {

    public val currentStage: GenerationStage?

    /**
     * Starts the generation process. Runs through the stages in order, until either there are no
     * more stages to run (the process is complete), or the process is stopped with [stop].
     *
     * Does nothing if already started.
     */
    public fun start()

    /**
     * Stops the generation process. This may or may not finish the current stage if it is in the
     * middle of being ran, but it will not run any further stages.
     *
     * Does nothing if already stopped.
     */
    public fun stop()

    /**
     * Advances the generation process by one stage. This is designed for very fine grained control
     * over the individual execution of each stage.
     *
     * This exists for debugging and testing purposes, as well as for if you want to, for example,
     * partially generate a world for curiosity.
     */
    public fun advance()
}
