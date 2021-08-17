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

/**
 * All of the built-in painting canvases.
 */
object Canvases {

    // @formatter:off
    @JvmField val ALBAN = get("alban")
    @JvmField val AZTEC = get("aztec")
    @JvmField val AZTEC2 = get("aztec2")
    @JvmField val BOMB = get("bomb")
    @JvmField val BURNING_SKULL = get("burning_skull")
    @JvmField val BUST = get("bust")
    @JvmField val COURBET = get("courbet")
    @JvmField val CREEBET = get("creebet")
    @JvmField val DONKEY_KONG = get("donkey_kong")
    @JvmField val FIGHTERS = get("fighters")
    @JvmField val GRAHAM = get("graham")
    @JvmField val KEBAB = get("kebab")
    @JvmField val MATCH = get("match")
    @JvmField val PIGSCENE = get("pigscene")
    @JvmField val PLANT = get("plant")
    @JvmField val POINTER = get("pointer")
    @JvmField val POOL = get("pool")
    @JvmField val SEA = get("sea")
    @JvmField val SKELETON = get("skeleton")
    @JvmField val SKULL_AND_ROSES = get("skull_and_roses")
    @JvmField val STAGE = get("stage")
    @JvmField val SUNSET = get("sunset")
    @JvmField val VOID = get("void")
    @JvmField val WANDERER = get("wanderer")
    @JvmField val WASTELAND = get("wasteland")
    @JvmField val WITHER = get("wither")

    // @formatter:on
    private fun get(name: String) = Registries.CANVAS[Key.key(name)]
}
