package org.kryptonmc.api.world.biome

import org.spongepowered.math.vector.Vector3i

/**
 * Something that contains biomes.
 */
public interface BiomeContainer {

    /**
     * Gets the biome at the given [x], [y], and [z] coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the biome
     */
    public fun getBiome(x: Int, y: Int, z: Int): Biome

    /**
     * Gets the biome at the given [position].
     *
     * @param position the position
     * @return the biome at the given position
     */
    public fun getBiome(position: Vector3i): Biome
}
