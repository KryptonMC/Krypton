package org.kryptonmc.api.world.chunk

/**
 * Promotes a proto chunk (a chunk that is still being generated) to a full chunk.
 *
 * This isn't actually part of the generation API, and less thought has gone in to its design.
 * It should be regarded as a possible, maybe, could be API, but not a core part of the API.
 */
public interface ChunkPromoter {

    public fun promote(chunk: ProtoChunk): Chunk
}
