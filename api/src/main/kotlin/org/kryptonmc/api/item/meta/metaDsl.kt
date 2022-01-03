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
package org.kryptonmc.api.item.meta

import org.jetbrains.annotations.Contract

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class MetaDsl

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
 * @param builder the builder
 * @param B the type of the builder
 * @param P the type of the metadata
 * @return new item metadata
 */
@MetaDsl
@JvmSynthetic
@JvmName("itemMetaGeneric")
@Contract("_ -> new", pure = true)
public inline fun <B : ItemMetaBuilder<B, P>, reified P : ItemMetaBuilder.Provider<B>> itemMeta(
    builder: B.() -> Unit
): P = ItemMeta.builder(P::class.java).apply(builder).build()

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
public inline fun fireworkRocketMeta(
    builder: FireworkRocketMeta.Builder.() -> Unit
): FireworkRocketMeta = FireworkRocketMeta.builder().apply(builder).build()

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
public inline fun fireworkStarMeta(builder: FireworkStarMeta.Builder.() -> Unit): FireworkStarMeta = FireworkStarMeta.builder().apply(builder).build()

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
public inline fun leatherArmorMeta(builder: LeatherArmorMeta.Builder.() -> Unit): LeatherArmorMeta = LeatherArmorMeta.builder().apply(builder).build()

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
public inline fun writableBookMeta(builder: WritableBookMeta.Builder.() -> Unit): WritableBookMeta = WritableBookMeta.builder().apply(builder).build()

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
