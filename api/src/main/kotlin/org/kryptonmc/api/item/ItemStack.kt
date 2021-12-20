/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
package org.kryptonmc.api.item

import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.api.item.meta.ItemMetaBuilder
import org.kryptonmc.api.util.provide
import java.util.function.Consumer

/**
 * A stack of items in an inventory.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ItemStack : Buildable<ItemStack, ItemStack.Builder> {

    /**
     * The type of item in this stack.
     */
    @get:JvmName("type")
    public val type: ItemType

    /**
     * The amount of items in this stack.
     */
    @get:JvmName("amount")
    public val amount: Int

    /**
     * The metadata for this item stack.
     */
    @get:JvmName("meta")
    public val meta: ItemMeta

    /**
     * Gets the metadata for this item stack as the given type [I], or returns
     * null if the metadata could not be casted to the given [type].
     *
     * @param type the type
     * @return the metadata as the type, or null if the metadata is not of the
     * type
     */
    public fun <I : ItemMeta> meta(type: Class<I>): I?

    /**
     * Creates a new item stack from the result of applying the given
     * [builder].
     *
     * @param builder the builder to apply
     * @return a new item stack
     */
    @JvmSynthetic
    @Contract("_ -> new", pure = true)
    public fun with(builder: Builder.() -> Unit): ItemStack

    /**
     * Creates a new item stack from the result of applying the given
     * [builder].
     *
     * @param builder the builder to apply
     * @return a new item stack
     */
    @Contract("_ -> new", pure = true)
    public fun with(builder: Consumer<Builder>): ItemStack = with { builder.accept(this) }

    /**
     * Creates a new item stack with the given [type].
     *
     * @param type the new type
     * @return a new item stack
     */
    @Contract("_ -> new", pure = true)
    public fun withType(type: ItemType): ItemStack

    /**
     * Creates a new item stack with the given [amount].
     *
     * @param amount the new amount
     * @return a new item stack
     */
    @Contract("_ -> new", pure = true)
    public fun withAmount(amount: Int): ItemStack

    /**
     * Grows the amount of this item stack by the given [amount] and returns
     * the resulting item stack.
     *
     * This will calculate the new amount by adding the given [amount] to the
     * current [ItemStack.amount].
     *
     * @param amount the amount to grow by
     * @return the resulting item stack
     */
    @Contract("_ -> new", pure = true)
    public fun grow(amount: Int): ItemStack = withAmount(this.amount + amount)

    /**
     * Shrinks the amount of this item stack by the given [amount] and returns
     * the resulting item stack.
     *
     * This will calculate the new amount by taking the given [amount] away
     * from the current [ItemStack.amount].
     *
     * @param amount the amount to shrink by
     * @return the resulting item stack
     */
    @Contract("_ -> new", pure = true)
    public fun shrink(amount: Int): ItemStack = withAmount(this.amount - amount)

    /**
     * Creates a new item stack with the given [meta].
     *
     * @param meta the new meta
     * @return a new item stack
     */
    public fun withMeta(meta: ItemMeta): ItemStack

    /**
     * Creates a new item stack with meta retrieved applying the given
     * [builder] to a new item metadata builder.
     *
     * @param builder the builder to apply
     * @return a new item stack
     */
    public fun withMeta(builder: ItemMeta.Builder.() -> Unit): ItemStack

    /**
     * Creates a new item stack with meta retrieved applying the given
     * [builder] to a new meta builder created with the given [type].
     *
     * @param type the type
     * @param builder the builder to apply
     * @param B the builder type
     * @param P the metadata type
     * @return a new item stack
     */
    @JvmSynthetic
    public fun <B : ItemMetaBuilder<B, P>, P : ItemMetaBuilder.Provider<B>> withMeta(type: Class<P>, builder: B.() -> Unit): ItemStack

    /**
     * Creates a new item stack with meta retrieved applying the given
     * [builder] to a new meta builder created with the given [type].
     *
     * @param type the type
     * @param builder the builder to apply
     * @param B the builder type
     * @param P the metadata type
     * @return a new item stack
     */
    public fun <B : ItemMetaBuilder<B, P>, P : ItemMetaBuilder.Provider<B>> withMeta(
        type: Class<P>,
        builder: Consumer<B>
    ): ItemStack = withMeta(type) { builder.accept(this) }

    /**
     * For building new [ItemStack]s.
     */
    @ItemDsl
    public interface Builder : Buildable.Builder<ItemStack> {

        /**
         * Sets the type of the [ItemStack] being built.
         *
         * @param type the type of the item
         * @return this builder
         */
        @ItemDsl
        @Contract("_ -> this", mutates = "this")
        public fun type(type: ItemType): Builder

        /**
         * Sets the amount of items in the [ItemStack] being built.
         *
         * @param amount the amount of items
         * @return this builder
         */
        @ItemDsl
        @Contract("_ -> this", mutates = "this")
        public fun amount(amount: Int): Builder

        /**
         * Sets the metadata for the item stack to the given [meta].
         *
         * @param meta the metadata
         * @return this builder
         */
        @ItemDsl
        @Contract("_ -> this", mutates = "this")
        public fun meta(meta: ItemMeta): Builder

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
        public fun meta(builder: ItemMeta.Builder.() -> Unit): Builder

        /**
         * Applies the given [builder] function to the metadata builder for
         * this builder.
         *
         * @param builder the builder function to apply
         * @return this builder
         */
        @ItemDsl
        @Contract("_ -> this", mutates = "this")
        public fun meta(builder: Consumer<ItemMeta.Builder>): Builder = meta { builder.accept(this) }

        /**
         * Applies the given [builder] function to the metadata builder for
         * this builder.
         *
         * @param type the type of the metadata
         * @param builder the builder function to apply
         * @param B the builder type
         * @param P the metadata type
         * @return this builder
         */
        @ItemDsl
        @JvmSynthetic
        @Contract("_ -> this", mutates = "this")
        public fun <B : ItemMetaBuilder<B, P>, P : ItemMetaBuilder.Provider<B>> meta(
            type: Class<P>,
            builder: B.() -> Unit
        ): Builder

        /**
         * Applies the given [builder] function to the metadata builder for
         * this builder.
         *
         * @param type the type of the metadata
         * @param builder the builder function to apply
         * @param B the builder type
         * @param P the metadata type
         * @return this builder
         */
        @ItemDsl
        @Contract("_ -> this", mutates = "this")
        public fun <B : ItemMetaBuilder<B, P>, P : ItemMetaBuilder.Provider<B>> meta(
            type: Class<P>,
            builder: Consumer<B>
        ): Builder = meta(type) { builder.accept(this) }

        /**
         * Builds a new [ItemStack] with the settings retrieved from
         * this builder.
         */
        @Contract("_ -> new", pure = true)
        override fun build(): ItemStack
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(): Builder

        public fun empty(): ItemStack
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new builder for building [ItemStack] instances.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(): Builder = FACTORY.builder()

        /**
         * Creates a new [ItemStack] with the given [type].
         *
         * @param type the type
         * @return a new item stack
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(type: ItemType): ItemStack = FACTORY.builder().type(type).build()

        /**
         * Creates a new [ItemStack] with the given [type] and [amount].
         *
         * @param type the type of item
         * @param amount the amount of items
         * @return a new [ItemStack]
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(type: ItemType, amount: Int): ItemStack = FACTORY.builder().type(type).amount(amount).build()

        /**
         * Gets the empty [ItemStack] instance.
         *
         * This [ItemStack] is generally used as a default value in
         * place of using `null`. It has an [ItemType] of [air][ItemTypes.AIR],
         * an amount of `1`, and no metadata.
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun empty(): ItemStack = FACTORY.empty()
    }
}

/**
 * Gets the metadata for this item stack as the given type [I], or returns
 * null if the metadata could not be casted to the given type [I].
 *
 * @return the metadata as the type, or null if the metadata is not of the
 * type
 */
@JvmSynthetic
public inline fun <reified I : ItemMeta> ItemStack.meta(): I? = meta(I::class.java)

/**
 * Creates a new item stack with meta retrieved applying the given
 * [builder] to a new meta builder created with the given type [P].
 *
 * @param builder the builder to apply
 * @param B the builder type
 * @param P the metadata type
 * @return a new item stack
 */
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun <B : ItemMetaBuilder<B, P>, reified P : ItemMetaBuilder.Provider<B>> ItemStack.withMeta(
    noinline builder: B.() -> Unit
): ItemStack = withMeta(P::class.java, builder)

/**
 * Applies the given [builder] function to the metadata builder for
 * this builder.
 *
 * @param builder the builder function to apply
 * @param B the builder type
 * @param P the metadata type
 * @return this builder
 */
@ItemDsl
@JvmSynthetic
@Contract("_ -> this", mutates = "this")
public inline fun <B : ItemMetaBuilder<B, P>, reified P : ItemMetaBuilder.Provider<B>> ItemStack.Builder.meta(
    noinline builder: B.() -> Unit
): ItemStack.Builder = meta(P::class.java, builder)
