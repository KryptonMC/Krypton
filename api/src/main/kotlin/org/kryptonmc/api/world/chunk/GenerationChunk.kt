package org.kryptonmc.api.world.chunk

import org.kryptonmc.api.block.BlockContainer
import org.kryptonmc.api.fluid.FluidContainer
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.biome.BiomeContainer

/**
 * A chunk that has not been fully generated, and is used in world generation.
 */
public interface GenerationChunk : BlockContainer, FluidContainer, BiomeContainer {

    public val world: World

    public val x: Int

    public val z: Int
}
