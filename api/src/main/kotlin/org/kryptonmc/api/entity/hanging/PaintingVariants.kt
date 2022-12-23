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
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All of the built-in vanilla painting variants.
 */
@Catalogue(PaintingVariant::class)
public object PaintingVariants {

    // @formatter:off
    @JvmField
    public val KEBAB: RegistryReference<PaintingVariant> = get("kebab")
    @JvmField
    public val AZTEC: RegistryReference<PaintingVariant> = get("aztec")
    @JvmField
    public val ALBAN: RegistryReference<PaintingVariant> = get("alban")
    @JvmField
    public val AZTEC2: RegistryReference<PaintingVariant> = get("aztec2")
    @JvmField
    public val BOMB: RegistryReference<PaintingVariant> = get("bomb")
    @JvmField
    public val PLANT: RegistryReference<PaintingVariant> = get("plant")
    @JvmField
    public val WASTELAND: RegistryReference<PaintingVariant> = get("wasteland")
    @JvmField
    public val POOL: RegistryReference<PaintingVariant> = get("pool")
    @JvmField
    public val COURBET: RegistryReference<PaintingVariant> = get("courbet")
    @JvmField
    public val SEA: RegistryReference<PaintingVariant> = get("sea")
    @JvmField
    public val SUNSET: RegistryReference<PaintingVariant> = get("sunset")
    @JvmField
    public val CREEBET: RegistryReference<PaintingVariant> = get("creebet")
    @JvmField
    public val WANDERER: RegistryReference<PaintingVariant> = get("wanderer")
    @JvmField
    public val GRAHAM: RegistryReference<PaintingVariant> = get("graham")
    @JvmField
    public val MATCH: RegistryReference<PaintingVariant> = get("match")
    @JvmField
    public val BUST: RegistryReference<PaintingVariant> = get("bust")
    @JvmField
    public val STAGE: RegistryReference<PaintingVariant> = get("stage")
    @JvmField
    public val VOID: RegistryReference<PaintingVariant> = get("void")
    @JvmField
    public val SKULL_AND_ROSES: RegistryReference<PaintingVariant> = get("skull_and_roses")
    @JvmField
    public val WITHER: RegistryReference<PaintingVariant> = get("wither")
    @JvmField
    public val FIGHTERS: RegistryReference<PaintingVariant> = get("fighters")
    @JvmField
    public val POINTER: RegistryReference<PaintingVariant> = get("pointer")
    @JvmField
    public val PIGSCENE: RegistryReference<PaintingVariant> = get("pigscene")
    @JvmField
    public val BURNING_SKULL: RegistryReference<PaintingVariant> = get("burning_skull")
    @JvmField
    public val SKELETON: RegistryReference<PaintingVariant> = get("skeleton")
    @JvmField
    public val DONKEY_KONG: RegistryReference<PaintingVariant> = get("donkey_kong")

    // @formatter:on
    @JvmStatic
    private fun get(name: String): RegistryReference<PaintingVariant> = RegistryReference.of(Registries.PAINTING_VARIANT, Key.key(name))
}
