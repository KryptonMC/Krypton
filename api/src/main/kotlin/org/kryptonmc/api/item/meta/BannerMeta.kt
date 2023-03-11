/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.item.meta

import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Unmodifiable
import org.kryptonmc.api.block.entity.banner.BannerPattern
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.dsl.MetaDsl

/**
 * Item metadata for a banner.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface BannerMeta : ScopedItemMeta<BannerMeta.Builder, BannerMeta> {

    /**
     * All of the patterns for this banner metadata.
     */
    @get:JvmName("patterns")
    public val patterns: @Unmodifiable List<BannerPattern>

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
    public fun withPattern(pattern: BannerPattern): BannerMeta

    /**
     * Creates new banner metadata with the pattern at the given [index]
     * removed from the list of patterns.
     *
     * @param index the index of the pattern to remove
     * @return new banner metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withoutPattern(index: Int): BannerMeta

    /**
     * Creates new banner metadata with the given [pattern] removed from the
     * list of patterns.
     *
     * @param pattern the pattern to remove
     * @return new banner metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withoutPattern(pattern: BannerPattern): BannerMeta

    /**
     * A builder for building banner metadata.
     */
    @MetaDsl
    public interface Builder : ItemMetaBuilder<Builder, BannerMeta> {

        /**
         * Sets the patterns of the banner metadata.
         *
         * @param patterns the patterns
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun patterns(patterns: Collection<BannerPattern>): Builder

        /**
         * Sets the patterns of the banner metadata.
         *
         * @param patterns the patterns
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun patterns(vararg patterns: BannerPattern): Builder = patterns(patterns.asList())

        /**
         * Adds the given [pattern] to the list of patterns for the banner
         * metadata.
         *
         * @param pattern the pattern to add
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun addPattern(pattern: BannerPattern): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building banner metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("-> new", pure = true)
        public fun builder(): Builder = ItemMeta.builder(BannerMeta::class.java)
    }
}
