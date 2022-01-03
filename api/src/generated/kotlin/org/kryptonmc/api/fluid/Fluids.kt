/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.fluid

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
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
