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
package org.kryptonmc.api.entity

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Contract

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class EntityTypeDsl

/**
 * Creates a new entity type with the given [key], [category], and the result
 * of applying the given [builder] function.
 *
 * @param key the key
 * @param category the category
 * @param builder the builder function to apply
 * @return a new entity type
 */
@EntityTypeDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun <T : Entity> entityType(key: Key, category: EntityCategory, builder: EntityType.Builder<T>.() -> Unit): EntityType<T> =
    EntityType.builder<T>(key, category).apply(builder).build()
