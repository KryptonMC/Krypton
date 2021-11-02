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
 * All of the built-in vanilla temperature modifiers.
 */
@Catalogue(TemperatureModifier::class)
public object TemperatureModifiers {

    // @formatter:off
    @JvmField public val NONE: TemperatureModifier = register("none")
    @JvmField public val FROZEN: TemperatureModifier = register("frozen")

    // @formatter:on
    @JvmStatic
    private fun register(name: String): TemperatureModifier {
        val key = Key.key(name)
        return Registries.TEMPERATURE_MODIFIERS.register(key, TemperatureModifier.of(key))
    }
}
