package org.kryptonmc.api.world.dimension

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla dimension effects.
 */
@Suppress("UndocumentedPublicProperty")
@Catalogue(DimensionEffect::class)
public object DimensionEffects {

    // @formatter:off
    @JvmField public val OVERWORLD: DimensionEffect = get("overworld")
    @JvmField public val THE_NETHER: DimensionEffect = get("the_nether")
    @JvmField public val THE_END: DimensionEffect = get("the_end")

    // @formatter:on
    private fun get(name: String): DimensionEffect = Registries.DIMENSION_EFFECTS[Key.key(name)]!!
}
