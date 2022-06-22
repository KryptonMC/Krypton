/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
@file:Suppress("MatchingDeclarationName")
package org.kryptonmc.api.world.biome

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Contract

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class BiomeDsl

/**
 * Creates a new biome with the given [key] and applies the given [builder] to
 * a new builder, then builds the result.
 *
 * @param key the key
 * @param builder the builder
 * @return a new biome
 */
@BiomeDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun biome(key: Key, builder: Biome.Builder.() -> Unit): Biome = Biome.builder(key).apply(builder).build()
