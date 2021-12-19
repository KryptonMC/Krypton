package org.kryptonmc.api.item.meta

import org.jetbrains.annotations.Contract
import java.awt.Color

/**
 * Item metadata for leather armour.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface LeatherArmorMeta : ScopedItemMeta<LeatherArmorMeta> {

    /**
     * The displayed colour of the item.
     */
    @get:JvmName("color")
    public val color: Color

    /**
     * Creates new item metadata with the given [color].
     *
     * @param color the new colour
     * @return new item metadata
     */
    public fun withColor(color: Color): ItemMeta

    /**
     * A builder for building leather armour metadata.
     */
    public interface Builder : ItemMetaBuilder<Builder, LeatherArmorMeta> {

        /**
         * Sets the colour of the leather armour to the given [color].
         *
         * @param color the colour
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun color(color: Color): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building leather armour metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        public fun builder(): Builder = ItemMeta.FACTORY.builder(LeatherArmorMeta::class.java)
    }
}
