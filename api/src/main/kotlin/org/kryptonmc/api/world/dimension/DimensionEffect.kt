package org.kryptonmc.api.world.dimension

import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.util.CataloguedBy

/**
 * An effect for a [DimensionType]. This defines the set of rendering features
 * that may occur in the dimension, such as clouds or fog.
 *
 * See [here](https://minecraft.fandom.com/wiki/Effect_(dimension)) for more
 * information.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(DimensionEffects::class)
public interface DimensionEffect : Keyed {

    /**
     * Whether this effect has clouds.
     */
    @get:JvmName("clouds")
    public val clouds: Boolean

    /**
     * Whether this effect has celestial bodies, such as the sun, moon, and
     * stars.
     */
    @get:JvmName("celestialBodies")
    public val celestialBodies: Boolean

    /**
     * Whether this effect has fog.
     */
    @get:JvmName("fog")
    public val fog: Boolean

    /**
     * Whether this effect's sky looks like the end, a.k.a it is a purple box
     * with a static texture.
     */
    @get:JvmName("endSky")
    public val endSky: Boolean
}
