/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Unmodifiable
import org.kryptonmc.api.item.ItemStack
import javax.annotation.concurrent.Immutable

/**
 * Item metadata for a bundle.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@Immutable
public interface BundleMeta : ScopedItemMeta<BundleMeta.Builder, BundleMeta> {

    /**
     * The items contained within the bundle.
     */
    @get:JvmName("items")
    public val items: @Unmodifiable List<ItemStack>

    /**
     * Creates new item metadata with the given [items].
     *
     * @param items the new items
     * @return the new metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withItems(items: List<ItemStack>): BundleMeta

    /**
     * Creates new item metadata with the given [item] added to the end of the
     * items list.
     *
     * @param item the item to add
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withItem(item: ItemStack): BundleMeta

    /**
     * Creates new item metadata with the item at the given [index] removed
     * from the items list.
     *
     * @param index the index of the item to remove
     * @return new item metadata
     * @throws IllegalArgumentException if the index would result in an out of
     * bounds exception, i.e. when it is too small or too big
     */
    @Contract("_ -> new", pure = true)
    public fun withoutItem(index: Int): BundleMeta

    /**
     * Creates new item metadata with the given [item] removed from the items
     * list.
     *
     * @param item the item to remove
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withoutItem(item: ItemStack): BundleMeta

    /**
     * A builder for building bundle metadata.
     */
    @MetaDsl
    public interface Builder : ItemMetaBuilder<Builder, BundleMeta> {

        /**
         * Sets the items held by the bundle to the given [items].
         *
         * @param items the items
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun items(items: Collection<ItemStack>): Builder

        /**
         * Sets the items held by the bundle to the given [items].
         *
         * @param items the items
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun items(vararg items: ItemStack): Builder = items(items.asList())

        /**
         * Adds the given [item] to the bundle.
         *
         * @param item the item to add
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun addItem(item: ItemStack): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building bundle metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("-> new", pure = true)
        public fun builder(): Builder = ItemMeta.builder(BundleMeta::class.java)
    }
}
