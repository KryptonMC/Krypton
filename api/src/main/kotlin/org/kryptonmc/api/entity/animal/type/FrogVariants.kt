/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal.type

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla frog variants.
 */
@Catalogue(FrogVariant::class)
public object FrogVariants {

    @JvmField
    public val TEMPERATE: FrogVariant = register("temperate")
    @JvmField
    public val WARM: FrogVariant = register("warm")
    @JvmField
    public val COLD: FrogVariant = register("cold")

    @JvmStatic
    private fun register(name: String): FrogVariant {
        val key = Key.key(name)
        return Registries.FROG_VARIANT.register(key, FrogVariant.of(key))
    }
}
