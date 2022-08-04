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

import org.jetbrains.annotations.Contract

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class DimensionTypeDsl

/**
 * Creates a new dimension type by applying the given [builder] to a new
 * dimension type builder and building the instance.
 *
 * @param builder the builder to apply
 * @return a new dimension type
 */
@DimensionTypeDsl
@Contract("_, _ -> new", pure = true)
public inline fun dimensionType(builder: DimensionType.Builder.() -> Unit): DimensionType = DimensionType.builder().apply(builder).build()
