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
