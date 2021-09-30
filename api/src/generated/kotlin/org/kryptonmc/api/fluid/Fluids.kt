package org.kryptonmc.api.fluid

import kotlin.String
import kotlin.Suppress
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Suppress("UndocumentedPublicProperty", "LargeClass")
@Catalogue(Fluid::class)
public object Fluids {

    // @formatter:off
    @JvmField public val EMPTY: Fluid = get("empty")
    @JvmField public val FLOWING_WATER: Fluid = get("flowing_water")
    @JvmField public val WATER: Fluid = get("water")
    @JvmField public val FLOWING_LAVA: Fluid = get("flowing_lava")
    @JvmField public val LAVA: Fluid = get("lava")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): Fluid = Registries.FLUID[Key.key(key)]!!
}
