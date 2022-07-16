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
package org.kryptonmc.api.item

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Contract

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class ItemTypeDsl

/**
 * Creates a new item type with the given [key] and the result of applying the
 * given [builder] function.
 *
 * This is the DSL for creating item **types**. For creating item **stacks**,
 * see [item].
 *
 * @param key the key
 * @param builder the builder function to apply
 * @return a new item type
 * @see item
 */
@ItemTypeDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun itemType(key: Key, builder: ItemType.Builder.() -> Unit): ItemType = ItemType.builder(key).apply(builder).build()
