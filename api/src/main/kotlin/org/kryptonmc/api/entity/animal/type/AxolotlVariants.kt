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
 * All of the built-in vanilla axolotl variants.
 */
@Catalogue(AxolotlVariant::class)
public object AxolotlVariants {

    // @formatter:off
    @JvmField public val LUCY: AxolotlVariant = register("lucy")
    @JvmField public val WILD: AxolotlVariant = register("wild")
    @JvmField public val GOLD: AxolotlVariant = register("gold")
    @JvmField public val CYAN: AxolotlVariant = register("cyan")
    @JvmField public val BLUE: AxolotlVariant = register("blue", false)

    // @formatter:on
    @JvmStatic
    private fun register(name: String, isCommon: Boolean = true): AxolotlVariant {
        val key = Key.key(name)
        return Registries.AXOLOTL_VARIANTS.register(key, AxolotlVariant.of(key, isCommon))
    }
}
