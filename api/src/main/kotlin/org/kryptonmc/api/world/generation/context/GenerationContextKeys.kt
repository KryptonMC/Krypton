package org.kryptonmc.api.world.generation.context

import io.leangen.geantyref.TypeToken
import org.kryptonmc.api.world.World
import java.util.Random

/**
 * Some possible built-in keys.
 *
 * This also functions as an example of how context keys could be defined and used.
 */
public object GenerationContextKeys {

    @JvmField
    public val WORLD: GenerationContextKey<World> = GenerationContextKey.of(TypeToken.get(World::class.java))
    @JvmField
    public val RANDOM: GenerationContextKey<Random> = GenerationContextKey.of(TypeToken.get(Random::class.java))
}
