/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.item.meta.MetaHolder
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
    public var amount: Int

    /**
     * The holder for the stack's metadata.
     */
    @get:JvmName("meta")
    public val meta: MetaHolder

    /**
     * Creates a copy of this item stack.
     */
    @Contract("_ -> new", pure = true)
    public fun copy(): ItemStack

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
         * Applies the given [builder] function to the meta holder
         * for this builder.
         *
         * @param builder the builder function to apply to the meta
         * holder
         * @return this builder
         */
        @ItemDsl
        @JvmSynthetic
        @Contract("_ -> this", mutates = "this")
        public fun meta(builder: MetaHolder.() -> Unit): Builder

        /**
         * Applies the given [builder] function to the meta holder
         * for this builder.
         *
         * @param builder the builder function to apply to the meta
         * holder
         * @return this builder
         */
        @ItemDsl
        @Contract("_ -> this", mutates = "this")
        public fun meta(builder: Consumer<MetaHolder>): Builder = meta { builder.accept(this) }

        /**
         * Builds a new [ItemStack] with the settings retrieved from
         * this builder.
         */
        @Contract("_ -> new", pure = true)
        override fun build(): ItemStack
    }

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.OverrideOnly
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
         * Creates a new [ItemStack] with the given [type], [amount],
         * and [metaBuilder].
         *
         * @param type the type of item
         * @param amount the amount of items
         * @param metaBuilder the meta builder
         * @return a new [ItemStack]
         */
        @JvmStatic
        @JvmSynthetic
        @Contract("_ -> new", pure = true)
        public fun of(
            type: ItemType,
            amount: Int,
            metaBuilder: MetaHolder.() -> Unit
        ): ItemStack = FACTORY.builder().type(type).amount(amount).meta(metaBuilder).build()

        /**
         * Creates a new [ItemStack] with the given [type], [amount],
         * and [metaBuilder].
         *
         * @param type the type of item
         * @param amount the amount of items
         * @param metaBuilder the meta builder
         * @return a new [ItemStack]
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(
            type: ItemType,
            amount: Int,
            metaBuilder: Consumer<MetaHolder>
        ): ItemStack = of(type, amount) { metaBuilder.accept(this) }

        /**
         * Gets the empty [ItemStack] instance.
         *
         * This [ItemStack] is generally used as a default value in
         * place of using `null`. It has an [ItemType] of [air][ItemTypes.AIR],
         * an amount of `1`, and no metadata.
         *
         * This is also immutable, meaning it either keeps no state, or the
         * state it keeps cannot be mutated. For example, attempting to set the
         * amount or the metadata should do nothing.
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun empty(): ItemStack = FACTORY.empty()
    }
}
