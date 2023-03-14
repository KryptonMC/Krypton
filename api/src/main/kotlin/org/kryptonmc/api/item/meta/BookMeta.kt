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

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Unmodifiable
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.dsl.MetaDsl

/**
 * Contains shared metadata between [WritableBookMeta] and [WrittenBookMeta].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public sealed interface BookMeta<B : BookMeta.Builder<B, I>, I : BookMeta<B, I>> : ScopedItemMeta<B, I> {

    /**
     * The pages written in the book.
     */
    @get:JvmName("pages")
    public val pages: @Unmodifiable List<Component>

    /**
     * Creates new item metadata with the given [pages].
     *
     * @param pages the new pages
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withPages(pages: Collection<Component>): I

    /**
     * Creates new item metadata with the given [page] added to the end of the
     * pages.
     *
     * @param page the page to add
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withPage(page: Component): I

    /**
     * Creates new item metadata with the page at the given [index] removed
     * from the pages.
     *
     * @param index the index of the page to remove
     * @return new item metadata
     * @throws IllegalArgumentException if the index would result in an out of
     * bounds exception, i.e. when it is too small or too big
     */
    @Contract("_ -> new", pure = true)
    public fun withoutPage(index: Int): I

    /**
     * Creates new item metadata with the given [page] removed from the pages.
     *
     * @param page the page to remove
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withoutPage(page: Component): I

    /**
     * A builder for building book metadata.
     */
    @MetaDsl
    public interface Builder<B : Builder<B, I>, I : BookMeta<B, I>> : ItemMetaBuilder<B, I> {

        /**
         * Sets the pages the book has to the given [pages].
         *
         * @param pages the pages
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun pages(pages: Collection<Component>): B

        /**
         * Sets the pages the book has to the given [pages].
         *
         * @param pages the pages
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun pages(vararg pages: Component): B = pages(pages.asList())

        /**
         * Adds the given [page] to the list of pages the book has.
         *
         * @param page the page to add
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun addPage(page: Component): B
    }
}
