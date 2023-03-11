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
package org.kryptonmc.api.item.meta

import org.jetbrains.annotations.Contract
import org.kryptonmc.internal.annotations.dsl.MetaDsl

/**
 * Creates new item metadata from the result of applying the given [builder]
 * function.
 *
 * @param builder the builder
 * @return new item metadata
 */
@MetaDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun itemMeta(builder: ItemMeta.Builder.() -> Unit): ItemMeta = ItemMeta.builder().apply(builder).build()

/**
 * Creates new item metadata of the given type [P] from the result of applying
 * the given [builder] function to a new builder of type [B].
 *
 * @param B the type of the builder
 * @param P the type of the metadata
 * @param builder the builder
 * @return new item metadata
 */
@MetaDsl
@JvmSynthetic
@JvmName("itemMetaGeneric")
@Contract("_ -> new", pure = true)
public inline fun <B : ItemMetaBuilder<B, P>, reified P : ItemMetaBuilder.Provider<B>> itemMeta(builder: B.() -> Unit): P =
    ItemMeta.builder(P::class.java).apply(builder).build()

/**
 * Creates new bundle metadata from the result of applying the given [builder]
 * function.
 *
 * @param builder the builder
 * @return new bundle metadata
 */
@MetaDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun bundleMeta(builder: BundleMeta.Builder.() -> Unit): BundleMeta = BundleMeta.builder().apply(builder).build()

/**
 * Creates new compass metadata from the result of applying the given
 * [builder] function.
 *
 * @param builder the builder
 * @return new compass metadata
 */
@MetaDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun compassMeta(builder: CompassMeta.Builder.() -> Unit): CompassMeta = CompassMeta.builder().apply(builder).build()

/**
 * Creates new crossbow metadata from the result of applying the given
 * [builder] function.
 *
 * @param builder the builder
 * @return new crossbow metadata
 */
@MetaDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun crossbowMeta(builder: CrossbowMeta.Builder.() -> Unit): CrossbowMeta = CrossbowMeta.builder().apply(builder).build()

/**
 * Creates new firework rocket metadata from the result of applying the given
 * [builder] function.
 *
 * @param builder the builder
 * @return new firework rocket metadata
 */
@MetaDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun fireworkRocketMeta(builder: FireworkRocketMeta.Builder.() -> Unit): FireworkRocketMeta =
    FireworkRocketMeta.builder().apply(builder).build()

/**
 * Creates new firework star metadata from the result of applying the given
 * [builder] function.
 *
 * @param builder the builder
 * @return new firework star metadata
 */
@MetaDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun fireworkStarMeta(builder: FireworkStarMeta.Builder.() -> Unit): FireworkStarMeta =
    FireworkStarMeta.builder().apply(builder).build()

/**
 * Creates new leather armour metadata from the result of applying the given
 * [builder] function.
 *
 * @param builder the builder
 * @return new leather armour metadata
 */
@MetaDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun leatherArmorMeta(builder: LeatherArmorMeta.Builder.() -> Unit): LeatherArmorMeta =
    LeatherArmorMeta.builder().apply(builder).build()

/**
 * Creates new player head metadata from the result of applying the given
 * [builder] function.
 *
 * @param builder the builder
 * @return new player head metadata
 */
@MetaDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun playerHeadMeta(builder: PlayerHeadMeta.Builder.() -> Unit): PlayerHeadMeta = PlayerHeadMeta.builder().apply(builder).build()

/**
 * Creates new writable book metadata from the result of applying the given
 * [builder] function.
 *
 * @param builder the builder
 * @return new writable book metadata
 */
@MetaDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun writableBookMeta(builder: WritableBookMeta.Builder.() -> Unit): WritableBookMeta =
    WritableBookMeta.builder().apply(builder).build()

/**
 * Creates new written book metadata from the result of applying the given
 * [builder] function.
 *
 * @param builder the builder
 * @return new written book metadata
 */
@MetaDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun writtenBookMeta(builder: WrittenBookMeta.Builder.() -> Unit): WrittenBookMeta = WrittenBookMeta.builder().apply(builder).build()
