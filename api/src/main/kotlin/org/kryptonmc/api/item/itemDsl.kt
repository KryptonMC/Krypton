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

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.api.item.meta.ItemMetaBuilder

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class ItemDsl

/**
 * Creates a new item stack from the result of applying the given [builder]
 * function.
 *
 * @param builder the builder
 * @return a new item stack
 */
@ItemDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun item(builder: ItemStack.Builder.() -> Unit): ItemStack = ItemStack.builder().apply(builder).build()

/**
 * Creates a new item stack with the given [type] and the result of applying
 * the given [builder] function.
 *
 * @param type the item type
 * @param builder the builder
 * @return a new item stack
 */
@ItemDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun item(type: ItemType, builder: ItemStack.Builder.() -> Unit): ItemStack = ItemStack.builder().type(type).apply(builder).build()

/**
 * Creates a new item stack with the given [type], [amount], and the result of
 * applying the given [builder] function.
 *
 * @param type the item type
 * @param amount the amount of items
 * @param builder the builder
 * @return a new item stack
 */
@ItemDsl
@JvmSynthetic
@Contract("_, _, _ -> new", pure = true)
public inline fun item(type: ItemType, amount: Int, builder: ItemStack.Builder.() -> Unit): ItemStack =
    ItemStack.builder().type(type).amount(amount).apply(builder).build()

/**
 * Creates a new item stack with the given [type], [amount], and meta built
 * from the given [meta] builder.
 *
 * @param type the item type
 * @param amount the amount of items
 * @param meta the meta builder
 * @return a new item stack
 */
@ItemDsl
@JvmSynthetic
@JvmName("itemWithMeta")
@Contract("_, _, _ -> new", pure = true)
public inline fun item(type: ItemType, amount: Int, meta: ItemMeta.Builder.() -> Unit): ItemStack =
    ItemStack.builder().type(type).amount(amount).meta(ItemMeta.builder().apply(meta).build()).build()

/**
 * Creates a new item stack with the given [type], [amount], and meta of the
 * given type [P] built from the given [meta] builder of the given type [B].
 *
 * @param B the type of the metadata builder
 * @param P the type of the metadata
 * @param type the item type
 * @param amount the amount of items
 * @param meta the meta builder
 * @return a new item stack
 */
@ItemDsl
@JvmSynthetic
@JvmName("itemWithMetaGeneric")
@Contract("_, _, _ -> new", pure = true)
public inline fun <B : ItemMetaBuilder<B, P>, reified P : ItemMetaBuilder.Provider<B>> item(
    type: ItemType,
    amount: Int,
    noinline meta: B.() -> Unit
): ItemStack = ItemStack.builder().type(type).amount(amount).meta(meta).build()
