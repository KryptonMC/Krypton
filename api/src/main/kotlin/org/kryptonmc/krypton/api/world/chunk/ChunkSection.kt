package org.kryptonmc.krypton.api.world.chunk

/**
 * Represents a section of a chunk, or a 16 x 16 x 16 area of blocks.
 *
 * @author Callum Seabrook
 */
interface ChunkSection {

    /**
     * The Y value of this chunk section (the position it is in the chunk)
     *
     * @return This section's Y value
     */
    val y: Int
}