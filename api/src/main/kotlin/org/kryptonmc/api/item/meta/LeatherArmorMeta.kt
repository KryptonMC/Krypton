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
import org.kryptonmc.api.util.Color
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Item metadata for leather armour.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface LeatherArmorMeta : ScopedItemMeta<LeatherArmorMeta.Builder, LeatherArmorMeta> {

    /**
     * The displayed colour of the item.
     */
    @get:JvmName("color")
    public val color: Color?

    /**
     * Creates new item metadata with the given [color].
     *
     * @param color the new colour
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withColor(color: Color?): LeatherArmorMeta

    /**
     * A builder for building leather armour metadata.
     */
    @MetaDsl
    public interface Builder : ItemMetaBuilder<Builder, LeatherArmorMeta> {

        /**
         * Sets the colour of the leather armour to the given [color].
         *
         * @param color the colour
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun color(color: Color?): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building leather armour metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("-> new", pure = true)
        public fun builder(): Builder = ItemMeta.builder(LeatherArmorMeta::class.java)
    }
}
