/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Unmodifiable
import org.kryptonmc.api.item.data.WrittenBookGeneration
import javax.annotation.concurrent.Immutable

/**
 * Item metadata for books that have been written.
 */
@Suppress("INAPPLICABLE_JVM_NAME", "NonExtendableApiUsage")
@Immutable
public interface WrittenBookMeta : BookMeta<WrittenBookMeta.Builder, WrittenBookMeta>, Book {

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
    override val pages: @Unmodifiable List<Component>

    /**
     * Creates new item metadata with the given [title].
     *
     * @param title the new title
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withTitle(title: Component): WrittenBookMeta

    /**
     * Creates new item metadata with the given [author].
     *
     * @param author the new author
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withAuthor(author: Component): WrittenBookMeta

    /**
     * Creates new item metadata with the given [generation].
     *
     * @param generation the new generation
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withGeneration(generation: WrittenBookGeneration): WrittenBookMeta

    override fun title(): Component = title

    override fun title(title: Component): WrittenBookMeta = withTitle(title)

    override fun author(): Component = author

    override fun author(author: Component): WrittenBookMeta = withAuthor(author)

    override fun pages(): List<Component> = pages

    override fun pages(pages: List<Component>): WrittenBookMeta = withPages(pages)

    override fun toBuilder(): Builder

    /**
     * A builder for building written book metadata.
     */
    @MetaDsl
    public interface Builder : BookMeta.Builder<Builder, WrittenBookMeta>, Book.Builder {

        /**
         * Sets the generation of the book to the given [generation].
         *
         * @param generation the generation
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun generation(generation: WrittenBookGeneration): Builder

        @MetaDsl
        override fun title(title: Component): Builder

        @MetaDsl
        override fun author(author: Component): Builder

        @MetaDsl
        override fun addPage(page: Component): Builder

        @MetaDsl
        override fun pages(pages: Collection<Component>): Builder

        @MetaDsl
        override fun pages(vararg pages: Component): Builder = pages(pages.asList())
    }

    public companion object {

        /**
         * Creates a new builder for building written book metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("-> new", pure = true)
        public fun builder(): Builder = ItemMeta.builder(WrittenBookMeta::class.java)
    }
}
