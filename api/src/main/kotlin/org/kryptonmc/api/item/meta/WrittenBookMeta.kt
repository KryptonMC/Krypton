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

import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Unmodifiable
import org.kryptonmc.api.item.data.WrittenBookGeneration
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.ImmutableTypeIgnore
import org.kryptonmc.internal.annotations.dsl.MetaDsl

/**
 * Item metadata for books that have been written.
 */
@Suppress("INAPPLICABLE_JVM_NAME", "NonExtendableApiUsage")
@ImmutableType
public interface WrittenBookMeta : BookMeta<WrittenBookMeta.Builder, WrittenBookMeta>, Book {

    /**
     * The title of the written book.
     */
    @get:JvmSynthetic
    @ImmutableTypeIgnore
    public val title: Component

    /**
     * The author of the written book.
     */
    @get:JvmSynthetic
    @ImmutableTypeIgnore
    public val author: Component

    /**
     * The generation of the written book.
     */
    @get:JvmName("generation")
    public val generation: WrittenBookGeneration

    @get:JvmSynthetic
    @ImmutableTypeIgnore
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
