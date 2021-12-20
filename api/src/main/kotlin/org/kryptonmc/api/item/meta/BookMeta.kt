/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract

/**
 * Contains shared metadata between [WritableBookMeta] and [WrittenBookMeta].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BookMeta<I : BookMeta<I>> : ScopedItemMeta<I> {

    /**
     * The pages written in the book.
     */
    @get:JvmName("pages")
    public val pages: List<Component>

    /**
     * Creates new item metadata with the given [pages].
     *
     * @param pages the new pages
     * @return new item metadata
     */
    public fun withPages(pages: Iterable<Component>): I

    /**
     * Creates new item metadata with the given [page] added to the end of the
     * pages.
     *
     * @param page the page to add
     * @return new item metadata
     */
    public fun addPage(page: Component): I

    /**
     * Creates new item metadata with the page at the given [index] removed
     * from the pages.
     *
     * @param index the index of the page to remove
     * @return new item metadata
     * @throws IllegalArgumentException if the index would result in an out of
     * bounds exception, i.e. when it is too small or too big
     */
    public fun removePage(index: Int): I

    /**
     * Creates new item metadata with the given [page] removed from the pages.
     *
     * @param page the page to remove
     * @return new item metadata
     */
    public fun removePage(page: Component): I

    /**
     * A builder for building book metadata.
     */
    public interface Builder<B : Builder<B, I>, I : BookMeta<I>> : ItemMetaBuilder<B, I> {

        /**
         * Sets the pages the book has to the given [pages].
         *
         * @param pages the pages
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun pages(pages: Iterable<Component>): B

        /**
         * Sets the pages the book has to the given [pages].
         *
         * @param pages the pages
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun pages(vararg pages: Component): B = pages(pages.asIterable())

        /**
         * Sets the pages the book has to the given [pages].
         *
         * @param pages the pages
         * @return this builder
         */
        @JvmSynthetic
        @JvmName("pagesArray")
        @Contract("_ -> this", mutates = "this")
        public fun pages(pages: Array<Component>): B = pages(pages.asIterable())

        /**
         * Adds the given [page] to the list of pages the book has.
         *
         * @param page the page to add
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun addPage(page: Component): B
    }
}
