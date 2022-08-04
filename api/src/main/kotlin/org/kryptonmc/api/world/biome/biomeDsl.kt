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

import org.jetbrains.annotations.Contract

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class BiomeDsl

/**
 * Creates a new biome by applying the given [builder] to a new builder, then
 * building the result.
 *
 * @param builder the builder
 * @return a new biome
 */
@BiomeDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun biome(builder: Biome.Builder.() -> Unit): Biome = Biome.builder().apply(builder).build()
