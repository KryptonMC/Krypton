/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal.type

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla fox types.
 */
@Catalogue(FoxType::class)
public object FoxTypes {

    // @formatter:off
    @JvmField public val RED: FoxType = register("red")
    @JvmField public val SNOW: FoxType = register("snow")

    // @formatter:on
    @JvmStatic
    private fun register(name: String): FoxType {
        val key = Key.key(name)
        return Registries.FOX_TYPES.register(key, FoxType.of(key))
    }
}
