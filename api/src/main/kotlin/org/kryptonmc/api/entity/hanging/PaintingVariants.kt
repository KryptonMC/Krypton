/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
