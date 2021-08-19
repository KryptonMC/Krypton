/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.fluid

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries

/**
 * All of the built-in fluids.
 */
object Fluids {

    // @formatter:off
    @JvmField val EMPTY = get("empty")
    @JvmField val WATER = get("water")
    @JvmField val FLOWING_WATER = get("flowing_water")
    @JvmField val LAVA = get("lava")
    @JvmField val FLOWING_LAVA = get("flowing_lava")

    // @formatter:on
    private fun get(name: String): Fluid = Registries.FLUID[Key.key(name)]!!
}
