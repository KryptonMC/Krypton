/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.item.data.WrittenBookGeneration

/**
 * Item metadata for books that have been written.
 */
@Suppress("INAPPLICABLE_JVM_NAME", "NonExtendableApiUsage")
public interface WrittenBookMeta : BookMeta<WrittenBookMeta>, Book, ItemMetaBuilder.Provider<WrittenBookMeta.Builder> {

    /**
     * The title of the written book.
     */
    @get:JvmSynthetic
    public val title: Component

    /**
     * The author of the written book.
     */
    @get:JvmSynthetic
    public val author: Component

    /**
     * The generation of the written book.
     */
    @get:JvmName("generation")
    public val generation: WrittenBookGeneration

    @get:JvmSynthetic
    override val pages: List<Component>

    /**
     * Creates new item metadata with the given [title].
     *
     * @param title the new title
     * @return new item metadata
     */
    public fun withTitle(title: Component): WrittenBookMeta

    /**
     * Creates new item metadata with the given [author].
     *
     * @param author the new author
     * @return new item metadata
     */
    public fun withAuthor(author: Component): WrittenBookMeta

    /**
     * Creates new item metadata with the given [generation].
     *
     * @param generation the new generation
     * @return new item metadata
     */
    public fun withGeneration(generation: WrittenBookGeneration): WrittenBookMeta

    override fun title(): Component = title

    override fun title(title: Component): WrittenBookMeta = withTitle(title)

    override fun author(): Component = author

    override fun author(author: Component): WrittenBookMeta = withAuthor(author)

    override fun pages(): List<Component> = pages

    override fun pages(pages: List<Component>): WrittenBookMeta = withPages(pages)

    override fun pages(vararg pages: Component): WrittenBookMeta = withPages(pages.toList())

    override fun toBuilder(): Builder

    /**
     * A builder for building written book metadata.
     */
    public interface Builder : BookMeta.Builder<Builder, WrittenBookMeta>, Book.Builder {

        /**
         * Sets the generation of the book to the given [generation].
         *
         * @param generation the generation
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun generation(generation: WrittenBookGeneration): Builder

        override fun title(title: Component): Builder

        override fun author(author: Component): Builder

        override fun addPage(page: Component): Builder

        override fun pages(pages: Collection<Component>): Builder = pages(pages.asIterable())

        override fun pages(vararg pages: Component): Builder = pages(pages.asIterable())
    }

    public companion object {

        /**
         * Creates a new builder for building written book metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        public fun builder(): Builder = ItemMeta.FACTORY.builder(WrittenBookMeta::class.java)
    }
}
