/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
package org.kryptonmc.api.item

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.api.item.meta.ItemMetaBuilder
import org.kryptonmc.internal.annotations.dsl.ItemDsl

/**
 * Gets the metadata for this item stack as the given type [I], or returns
 * null if the metadata could not be casted to the given type [I].
 *
 * @param I the metadata type
 * @return the metadata as the type, or null if the metadata is not of the type
 */
@JvmSynthetic
public inline fun <reified I : ItemMeta> ItemStack.meta(): I? = meta(I::class.java)

/**
 * Creates a new item stack with meta retrieved applying the given
 * [builder] to a new item metadata builder.
 *
 * @param builder the builder to apply
 * @return a new item stack
 */
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun ItemStack.withMeta(builder: ItemMeta.Builder.() -> Unit): ItemStack = withMeta(ItemMeta.builder().apply(builder).build())

/**
 * Creates a new item stack with meta retrieved applying the given
 * [builder] to a new meta builder created with the given type [P].
 *
 * @param B the builder type
 * @param P the metadata type
 * @param builder the builder to apply
 * @return a new item stack
 */
@JvmSynthetic
@Contract("_ -> new", pure = true)
@JvmName("withMetaGeneric")
public inline fun <B : IMB<B, P>, reified P> ItemStack.withMeta(builder: B.() -> Unit): ItemStack where P : IMP<B>, P : ItemMeta =
    withMeta(ItemMeta.builder(P::class.java).apply(builder).build())

/**
 * Applies the given [builder] function to the metadata builder for
 * this builder.
 *
 * @param builder the builder function to apply
 * @return this builder
 */
@ItemDsl
@JvmSynthetic
@Contract("_ -> this", mutates = "this")
public inline fun ItemStack.Builder.meta(builder: ItemMeta.Builder.() -> Unit): ItemStack.Builder = meta(ItemMeta.builder().apply(builder).build())

/**
 * Applies the given [builder] function to the metadata builder for
 * this builder.
 *
 * @param B the builder type
 * @param P the metadata type
 * @param builder the builder function to apply
 * @return this builder
 */
@ItemDsl
@JvmSynthetic
@Contract("_ -> this", mutates = "this")
@JvmName("metaGeneric")
public inline fun <B : IMB<B, P>, reified P> ItemStack.Builder.meta(builder: B.() -> Unit): ItemStack.Builder where P : IMP<B>, P : ItemMeta =
    meta(ItemMeta.builder(P::class.java).apply(builder).build())

private typealias IMB<B, P> = ItemMetaBuilder<B, P>
private typealias IMP<T> = ItemMetaBuilder.Provider<T>
