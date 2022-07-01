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
import org.kryptonmc.api.block.entity.banner.BannerPattern

/**
 * Item metadata for a banner.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BannerMeta : ScopedItemMeta<BannerMeta.Builder, BannerMeta> {

    /**
     * All of the patterns for this banner metadata.
     */
    @get:JvmName("patterns")
    public val patterns: List<BannerPattern>

    /**
     * Creates new banner metadata with the given [patterns].
     *
     * @param patterns the patterns
     * @return new banner metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withPatterns(patterns: List<BannerPattern>): BannerMeta

    /**
     * Creates new banner metadata with the given [pattern] added to the list
     * of patterns.
     *
     * @param pattern the pattern to add
     * @return new banner metadata
     */
    @Contract("_ -> new", pure = true)
    public fun addPattern(pattern: BannerPattern): BannerMeta

    /**
     * Creates new banner metadata with the pattern at the given [index]
     * removed from the list of patterns.
     *
     * @param index the index of the pattern to remove
     * @return new banner metadata
     */
    @Contract("_ -> new", pure = true)
    public fun removePattern(index: Int): BannerMeta

    /**
     * Creates new banner metadata with the given [pattern] removed from the
     * list of patterns.
     *
     * @param pattern the pattern to remove
     * @return new banner metadata
     */
    @Contract("_ -> new", pure = true)
    public fun removePattern(pattern: BannerPattern): BannerMeta

    /**
     * A builder for building banner metadata.
     */
    public interface Builder : ItemMetaBuilder<Builder, BannerMeta> {

        /**
         * Sets the patterns of the banner metadata.
         *
         * @param patterns the patterns
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun patterns(patterns: List<BannerPattern>): Builder

        /**
         * Adds the given [pattern] to the list of patterns for the banner
         * metadata.
         *
         * @param pattern the pattern to add
         * @return this builder
         */
        @Contract("_ -> new", pure = true)
        public fun addPattern(pattern: BannerPattern): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building banner metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        public fun builder(): Builder = ItemMeta.FACTORY.builder(BannerMeta::class.java)
    }
}
