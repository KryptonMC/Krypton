/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
@file:Suppress("MatchingDeclarationName")
package org.kryptonmc.api.item

import org.jetbrains.annotations.Contract

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class ItemDsl

/**
 * Constructs a new [ItemStack] using the provided [builder] function.
 *
 * @param builder the builder
 * @return a new [ItemStack]
 */
@ItemDsl
@JvmSynthetic
@Contract("_ -> new")
public inline fun item(builder: ItemStack.Builder.() -> Unit): ItemStack = ItemStack.builder().apply(builder).build()

/**
 * Constructs a new [ItemStack] using the provided item [type] and [builder]
 * function.
 *
 * @param type the item type
 * @param builder the builder
 * @return a new [ItemStack]
 */
@ItemDsl
@JvmSynthetic
@Contract("_ -> new")
public inline fun item(
    type: ItemType,
    builder: ItemStack.Builder.() -> Unit
): ItemStack = ItemStack.builder().type(type).apply(builder).build()

/**
 * Constructs a new [ItemStack] using the provided item [type], [amount], and
 * [builder] function.
 *
 * @param type the item type
 * @param amount the amount of items
 * @param builder the builder
 * @return a new [ItemStack]
 */
@ItemDsl
@JvmSynthetic
@Contract("_ -> new")
public inline fun item(
    type: ItemType,
    amount: Int,
    builder: ItemStack.Builder.() -> Unit
): ItemStack = ItemStack.builder().type(type).amount(amount).apply(builder).build()
