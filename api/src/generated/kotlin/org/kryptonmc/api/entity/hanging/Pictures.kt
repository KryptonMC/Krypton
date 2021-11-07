/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.hanging

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(Picture::class)
public object Pictures {

    // @formatter:off
    @JvmField public val KEBAB: Picture = get("kebab")
    @JvmField public val AZTEC: Picture = get("aztec")
    @JvmField public val ALBAN: Picture = get("alban")
    @JvmField public val AZTEC2: Picture = get("aztec2")
    @JvmField public val BOMB: Picture = get("bomb")
    @JvmField public val PLANT: Picture = get("plant")
    @JvmField public val WASTELAND: Picture = get("wasteland")
    @JvmField public val POOL: Picture = get("pool")
    @JvmField public val COURBET: Picture = get("courbet")
    @JvmField public val SEA: Picture = get("sea")
    @JvmField public val SUNSET: Picture = get("sunset")
    @JvmField public val CREEBET: Picture = get("creebet")
    @JvmField public val WANDERER: Picture = get("wanderer")
    @JvmField public val GRAHAM: Picture = get("graham")
    @JvmField public val MATCH: Picture = get("match")
    @JvmField public val BUST: Picture = get("bust")
    @JvmField public val STAGE: Picture = get("stage")
    @JvmField public val VOID: Picture = get("void")
    @JvmField public val SKULL_AND_ROSES: Picture = get("skull_and_roses")
    @JvmField public val WITHER: Picture = get("wither")
    @JvmField public val FIGHTERS: Picture = get("fighters")
    @JvmField public val POINTER: Picture = get("pointer")
    @JvmField public val PIGSCENE: Picture = get("pigscene")
    @JvmField public val BURNING_SKULL: Picture = get("burning_skull")
    @JvmField public val SKELETON: Picture = get("skeleton")
    @JvmField public val DONKEY_KONG: Picture = get("donkey_kong")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): Picture = Registries.PICTURE[Key.key(key)]!!
}
