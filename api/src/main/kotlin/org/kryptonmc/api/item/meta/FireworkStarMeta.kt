/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.item.data.FireworkEffect

/**
 * Item metadata for a firework star.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface FireworkStarMeta : ScopedItemMeta<FireworkStarMeta>, ItemMetaBuilder.Provider<FireworkStarMeta.Builder> {

    /**
     * The effect displayed when the star explodes.
     */
    @get:JvmName("effect")
    public val effect: FireworkEffect?

    /**
     * Creates new item metadata with the given [effect].
     *
     * @param effect the new effect
     * @return new item metadata
     */
    public fun withEffect(effect: FireworkEffect?): FireworkStarMeta

    /**
     * A builder for building firework star metadata.
     */
    public interface Builder : ItemMetaBuilder<Builder, FireworkStarMeta> {

        /**
         * Sets the effect of the star to the given [effect].
         *
         * @param effect the effect
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun effect(effect: FireworkEffect?): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building bundle metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        public fun builder(): Builder = ItemMeta.FACTORY.builder(FireworkStarMeta::class.java)
    }
}
