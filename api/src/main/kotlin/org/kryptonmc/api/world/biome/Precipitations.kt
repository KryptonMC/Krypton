/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla precipitations.
 */
@Suppress("UndocumentedPublicProperty")
@Catalogue(Precipitation::class)
public object Precipitations {

    // @formatter:off
    @JvmField public val NONE: Precipitation = register("none")
    @JvmField public val RAIN: Precipitation = register("rain")
    @JvmField public val SNOW: Precipitation = register("snow")

    // @formatter:on
    @JvmStatic
    private fun register(name: String): Precipitation {
        val key = Key.key(name)
        return Registries.PRECIPITATIONS.register(key, Precipitation.of(key))
    }
}
