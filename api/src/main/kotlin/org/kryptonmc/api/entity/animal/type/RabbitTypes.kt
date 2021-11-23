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
 * All of the built-in vanilla rabbit types.
 */
@Catalogue(RabbitType::class)
public object RabbitTypes {

    // @formatter:off
    @JvmField public val BROWN: RabbitType = register("brown")
    @JvmField public val WHITE: RabbitType = register("white")
    @JvmField public val BLACK: RabbitType = register("black")
    @JvmField public val BLACK_AND_WHITE: RabbitType = register("black_and_white")
    @JvmField public val GOLD: RabbitType = register("gold")
    @JvmField public val SALT_AND_PEPPER: RabbitType = register("salt_and_pepper")
    @JvmField public val KILLER: RabbitType = register("killer")

    // @formatter:on
    @JvmStatic
    private fun register(name: String): RabbitType {
        val key = Key.key(name)
        return Registries.RABBIT_TYPES.register(key, RabbitType.of(key))
    }
}
