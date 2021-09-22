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
import org.kryptonmc.api.item.meta.MetaHolder
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.util.provide

/**
 * A stack of items in an inventory.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ItemStack : Buildable<ItemStack, ItemStack.Builder> {

    /**
     * The type of item in this stack.
     */
    public val type: ItemType

    /**
     * The amount of items in this stack.
     */
    public var amount: Int

    /**
     * The holder for the stack's metadata.
     */
    public val meta: MetaHolder

    /**
     * Creates a copy of this item stack.
     */
    @Contract("_ -> new")
    public fun copy(): ItemStack

    /**
     * For building new [ItemStack]s.
     */
    public interface Builder : Buildable.Builder<ItemStack> {

        /**
         * Sets the type of the [ItemStack] being built.
         *
         * @param type the type of the item
         * @return this builder
         */
        @Contract("_ -> this")
        public fun type(type: ItemType): Builder

        /**
         * Sets the amount of items in the [ItemStack] being built.
         *
         * @param amount the amount of items
         * @return this builder
         */
        @Contract("_ -> this")
        public fun amount(amount: Int): Builder

        /**
         * Applies the given [builder] function to the meta holder
         * for this builder.
         *
         * @param builder the builder function to apply to the meta
         * holder
         * @return this builder
         */
        @Contract("_ -> this")
        public fun meta(builder: MetaHolder.() -> Unit): Builder

        /**
         * Builds a new [ItemStack] with the settings retrieved from
         * this builder.
         */
        @Contract("_ -> new")
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

        private val FACTORY = FactoryProvider.INSTANCE.provide<Factory>()

        /**
         * Creates a new builder for building [ItemStack] instances.
         */
        @Contract("_ -> new")
        @JvmStatic
        public fun builder(): Builder = FACTORY.builder()

        /**
         * Creates a new [ItemStack] with the given [type].
         */
        @Contract("_ -> new")
        @JvmStatic
        public fun of(type: ItemType): ItemStack = FACTORY.builder().type(type).build()

        /**
         * Creates a new [ItemStack] with the given [type] and [amount].
         *
         * @param type the type of item
         * @param amount the amount of items
         * @return a new [ItemStack]
         */
        @Contract("_ -> new")
        @JvmStatic
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
        @Contract("_ -> new")
        @JvmStatic
        public fun of(
            type: ItemType,
            amount: Int,
            metaBuilder: MetaHolder.() -> Unit
        ): ItemStack = FACTORY.builder().type(type).amount(amount).meta(metaBuilder).build()

        /**
         * Gets the empty [ItemStack] instance.
         *
         * This [ItemStack] is generally used as a default value in
         * place of using `null`. It has an [ItemType] of [air][ItemTypes.AIR],
         * an amount of `1`, and no metadata. Metadata can also not
         * be retrieved or set on this object.
         */
        @JvmStatic
        public fun empty(): ItemStack = FACTORY.empty()
    }
}
