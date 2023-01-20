package org.kryptonmc.api.world.generation.context

/**
 * Context for world generation.
 *
 * This is designed to allow the pipeline to pass information to the stages (global context),
 * and for stages to be able to pass data to each other (local context).
 *
 * Some stages may require data that can only be provided after other stages are completed.
 *
 * For example, the structure references stage in vanilla requires the starts for the structures
 * from the structure starts stage. This information can be passed between the stages using
 * the context, which means it does not have to be a detail of the chunk itself, and the API
 * is more flexible, as it needs no knowledge that structures even exist.
 *
 * The way the API is designed, it is not clear whether some data is global or local. This is intentional,
 * to discourage reliance on where the data comes from.
 *
 * It is not yet clear how the API will handle the case where a stage requires data from another stage that is
 * not present, usually because it did not run or it was not added to the pipeline.
 *
 * We could either decide to fail the generation in some way, by throwing an exception, adding a
 * result to the stages, or skipping the stage, or we could leave it up to the stage to provide
 * alternative data for what it needs. We could also provide a way to specify that a stage requires
 * another to be in the pipeline, but this would defeat the purpose of the context API.
 */
public interface GenerationContext {

    public fun has(key: GenerationContextKey<*>): Boolean

    public fun <T> get(key: GenerationContextKey<T>): T?

    public fun <T> set(key: GenerationContextKey<T>, value: T)
}
