package org.kryptonmc.api.world.biome

import org.spongepowered.math.vector.Vector3i

/**
 * A generator for generating biomes.
 *
 * This must meet the following requirements:
 * * It must be fast: This generator may be used hundreds of times per chunk,
 *   and an implementation may be generating a lot of chunks very quickly if
 *   players are moving very fast.
 * * It must be thread-safe: This generator may be called from different
 *   threads, commonly independently and concurrently. Avoid performing any
 *   operations that may result in concurrency issues.
 * * It must be consistent: Every call to this biome generator with the same
 *   three coordinates must return the same biome. This will ensure that
 *   regeneration of chunks, or generation of duplicate worlds, will not cause
 *   any strange issues.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BiomeGenerator {

    /**
     * All of the biomes that this generator may generate.
     */
    @get:JvmName("biomes")
    public val biomes: List<Biome>

    /**
     * Generates a biome for the given [x], [y], and [z] coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the generated biome
     */
    public fun generate(x: Int, y: Int, z: Int): Biome

    /**
     * Generates a biome for the given [position].
     *
     * @param position the position
     * @return the generated biome
     */
    public fun generate(position: Vector3i): Biome = generate(position.x(), position.y(), position.z())
}
