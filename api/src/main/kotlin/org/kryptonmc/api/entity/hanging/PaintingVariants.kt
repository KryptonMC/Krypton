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
 * All of the built-in vanilla painting variants
 */
@Catalogue(PaintingVariant::class)
public object PaintingVariants {

    // @formatter:off
    @JvmField
    public val KEBAB: PaintingVariant = get("kebab")
    @JvmField
    public val AZTEC: PaintingVariant = get("aztec")
    @JvmField
    public val ALBAN: PaintingVariant = get("alban")
    @JvmField
    public val AZTEC2: PaintingVariant = get("aztec2")
    @JvmField
    public val BOMB: PaintingVariant = get("bomb")
    @JvmField
    public val PLANT: PaintingVariant = get("plant")
    @JvmField
    public val WASTELAND: PaintingVariant = get("wasteland")
    @JvmField
    public val POOL: PaintingVariant = get("pool")
    @JvmField
    public val COURBET: PaintingVariant = get("courbet")
    @JvmField
    public val SEA: PaintingVariant = get("sea")
    @JvmField
    public val SUNSET: PaintingVariant = get("sunset")
    @JvmField
    public val CREEBET: PaintingVariant = get("creebet")
    @JvmField
    public val WANDERER: PaintingVariant = get("wanderer")
    @JvmField
    public val GRAHAM: PaintingVariant = get("graham")
    @JvmField
    public val MATCH: PaintingVariant = get("match")
    @JvmField
    public val BUST: PaintingVariant = get("bust")
    @JvmField
    public val STAGE: PaintingVariant = get("stage")
    @JvmField
    public val VOID: PaintingVariant = get("void")
    @JvmField
    public val SKULL_AND_ROSES: PaintingVariant = get("skull_and_roses")
    @JvmField
    public val WITHER: PaintingVariant = get("wither")
    @JvmField
    public val FIGHTERS: PaintingVariant = get("fighters")
    @JvmField
    public val POINTER: PaintingVariant = get("pointer")
    @JvmField
    public val PIGSCENE: PaintingVariant = get("pigscene")
    @JvmField
    public val BURNING_SKULL: PaintingVariant = get("burning_skull")
    @JvmField
    public val SKELETON: PaintingVariant = get("skeleton")
    @JvmField
    public val DONKEY_KONG: PaintingVariant = get("donkey_kong")

    // @formatter:on
    @JvmStatic
    private fun get(name: String): PaintingVariant = Registries.PAINTING_VARIANT.get(Key.key(name))
}
