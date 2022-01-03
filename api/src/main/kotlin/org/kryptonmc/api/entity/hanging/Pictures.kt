/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.hanging

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla pictures
 */
@Catalogue(Picture::class)
public object Pictures {

    // @formatter:off
    @JvmField public val KEBAB: Picture = register("kebab", 16, 16)
    @JvmField public val AZTEC: Picture = register("aztec", 16, 16)
    @JvmField public val ALBAN: Picture = register("alban", 16, 16)
    @JvmField public val AZTEC2: Picture = register("aztec2", 16, 16)
    @JvmField public val BOMB: Picture = register("bomb", 16, 16)
    @JvmField public val PLANT: Picture = register("plant", 16, 16)
    @JvmField public val WASTELAND: Picture = register("wasteland", 16, 16)
    @JvmField public val POOL: Picture = register("pool", 32, 16)
    @JvmField public val COURBET: Picture = register("courbet", 32, 16)
    @JvmField public val SEA: Picture = register("sea", 32, 16)
    @JvmField public val SUNSET: Picture = register("sunset", 32, 16)
    @JvmField public val CREEBET: Picture = register("creebet", 32, 16)
    @JvmField public val WANDERER: Picture = register("wanderer", 16, 32)
    @JvmField public val GRAHAM: Picture = register("graham", 16, 32)
    @JvmField public val MATCH: Picture = register("match", 32, 32)
    @JvmField public val BUST: Picture = register("bust", 32, 32)
    @JvmField public val STAGE: Picture = register("stage", 32, 32)
    @JvmField public val VOID: Picture = register("void", 32, 32)
    @JvmField public val SKULL_AND_ROSES: Picture = register("skull_and_roses", 32, 32)
    @JvmField public val WITHER: Picture = register("wither", 32, 32)
    @JvmField public val FIGHTERS: Picture = register("fighters", 64, 32)
    @JvmField public val POINTER: Picture = register("pointer", 64, 64)
    @JvmField public val PIGSCENE: Picture = register("pigscene", 64, 64)
    @JvmField public val BURNING_SKULL: Picture = register("burning_skull", 64, 64)
    @JvmField public val SKELETON: Picture = register("skeleton", 64, 48)
    @JvmField public val DONKEY_KONG: Picture = register("donkey_kong", 64, 48)

    // @formatter:on
    @JvmStatic
    private fun register(name: String, width: Int, height: Int): Picture {
        val key = Key.key(name)
        return Registries.PICTURES.register(key, Picture.of(key, width, height))
    }
}
