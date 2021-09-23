package org.kryptonmc.api.world.dimension

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries

/**
 * All of the built-in vanilla dimension types.
 */
@Suppress("UndocumentedPublicProperty")
public object DimensionTypes {

    // @formatter:off
    @JvmField public val OVERWORLD: DimensionType = get("overworld")
    @JvmField public val OVERWORLD_CAVES: DimensionType = get("overworld_caves")
    @JvmField public val THE_NETHER: DimensionType = get("the_nether")
    @JvmField public val THE_END: DimensionType = get("the_end")

    // @formatter:on
    private fun get(name: String): DimensionType = Registries.DIMENSION_TYPES[Key.key(name)]!!
}
