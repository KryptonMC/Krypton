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
public inline fun <B : ItemMetaBuilder<B, P>, reified P> item(type: ItemType, amount: Int, meta: B.() -> Unit): ItemStack
where P : ItemMetaBuilder.Provider<B>, P : ItemMeta {
    return ItemStack.builder().type(type).amount(amount).meta(meta).build()
}
