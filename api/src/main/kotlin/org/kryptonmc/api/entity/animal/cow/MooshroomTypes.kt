/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal.cow

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla mooshroom types.
 */
@Catalogue(MooshroomType::class)
public object MooshroomTypes {

    // @formatter:off
    @JvmField public val BROWN: MooshroomType = register("brown")
    @JvmField public val RED: MooshroomType = register("red")

    // @formatter:on
    @JvmStatic
    private fun register(name: String): MooshroomType {
        val key = Key.key(name)
        return Registries.MOOSHROOM_TYPES.register(key, MooshroomType.of(key))
    }
}
