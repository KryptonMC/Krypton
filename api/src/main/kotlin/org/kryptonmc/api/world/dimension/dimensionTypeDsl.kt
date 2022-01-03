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
package org.kryptonmc.api.world.dimension

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Contract

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class DimensionTypeDsl

/**
 * Creates a new dimension type with the given [key], applying the given
 * [builder] to a new dimension type builder and building the instance.
 *
 * @param key the key
 * @param builder the builder to apply
 * @return a new dimension type
 */
@DimensionTypeDsl
@Contract("_ -> new", pure = true)
public inline fun dimensionType(
    key: Key,
    builder: DimensionType.Builder.() -> Unit
): DimensionType = DimensionType.builder(key).apply(builder).build()
