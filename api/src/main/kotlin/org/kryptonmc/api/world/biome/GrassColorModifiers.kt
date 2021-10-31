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
 * All of the built-in vanilla grass colour modifiers.
 */
@Suppress("UndocumentedPublicProperty")
@Catalogue(GrassColorModifier::class)
public object GrassColorModifiers {

    // @formatter:off
    @JvmField public val NONE: GrassColorModifier = register("none")
    @JvmField public val DARK_FOREST: GrassColorModifier = register("dark_forest")
    @JvmField public val SWAMP: GrassColorModifier = register("swamp")

    // @formatter:on
    @JvmStatic
    private fun register(name: String): GrassColorModifier {
        val key = Key.key(name)
        return Registries.GRASS_COLOR_MODIFIERS.register(key, GrassColorModifier.of(key))
    }
}
