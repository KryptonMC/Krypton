package org.kryptonmc.api.item.meta

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.item.ItemStack

/**
 * Item metadata for a bundle.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BundleMeta : ScopedItemMeta<BundleMeta> {

    /**
     * The items contained within the bundle.
     */
    @get:JvmName("items")
    public val items: List<ItemStack>

    /**
     * Creates new item metadata with the given [items].
     *
     * @param items the new items
     * @return the new metadata
     */
    public fun withItems(items: List<ItemStack>): BundleMeta

    /**
     * Creates new item metadata with the given [item] added to the end of the
     * items list.
     *
     * @param item the item to add
     * @return new item metadata
     */
    public fun addItem(item: ItemStack): BundleMeta

    /**
     * Creates new item metadata with the item at the given [index] removed
     * from the items list.
     *
     * @param index the index of the item to remove
     * @return new item metadata
     * @throws IllegalArgumentException if the index would result in an out of
     * bounds exception, i.e. when it is too small or too big
     */
    public fun removeItem(index: Int): BundleMeta

    /**
     * Creates new item metadata with the given [item] removed from the items
     * list.
     *
     * @param item the item to remove
     * @return new item metadata
     */
    public fun removeItem(item: ItemStack): ItemMeta

    /**
     * A builder for building bundle metadata.
     */
    public interface Builder : ItemMetaBuilder<Builder, BundleMeta> {

        /**
         * Sets the items held by the bundle to the given [items].
         *
         * @param items the items
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun items(items: Iterable<ItemStack>): Builder

        /**
         * Sets the items held by the bundle to the given [items].
         *
         * @param items the items
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun items(vararg items: ItemStack): Builder

        /**
         * Sets the items held by the bundle to the given [items].
         *
         * @param items the items
         * @return this builder
         */
        @JvmSynthetic
        @JvmName("itemsArray")
        @Contract("_ -> this", mutates = "this")
        public fun items(items: Array<ItemStack>): Builder

        /**
         * Adds the given [item] to the bundle.
         *
         * @param item the item to add
         * @return this builder
         */
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
        public fun builder(): Builder = ItemMeta.FACTORY.builder(BundleMeta::class.java)
    }
}
