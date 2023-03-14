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
package org.kryptonmc.krypton.adventure

import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.kryptonmc.api.item.meta.WrittenBookMeta
import org.kryptonmc.krypton.item.meta.KryptonWrittenBookMeta
import org.kryptonmc.krypton.testutil.Bootstrapping
import org.kryptonmc.nbt.CompoundTag
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class BookConversionTest {

    @Test
    fun `ensure title is preserved when converted to item metadata`() {
        val title = Component.text("title")
        val meta = KryptonAdventure.toItemStack(Book.book(title, Component.empty(), emptyList())).meta(WrittenBookMeta::class.java)!!
        assertEquals(title, meta.title())
    }

    @Test
    fun `ensure author is preserved when converted to item metadata`() {
        val author = Component.text("author")
        val meta = KryptonAdventure.toItemStack(Book.book(Component.empty(), author, emptyList())).meta(WrittenBookMeta::class.java)!!
        assertEquals(author, meta.author())
    }

    @Test
    fun `ensure pages are preserved when converted to item metadata`() {
        val pages = listOf(Component.text("Page 1"), Component.text("Page 2"), Component.text("Page 3"))
        val meta = KryptonAdventure.toItemStack(Book.book(Component.empty(), Component.empty(), pages)).meta(WrittenBookMeta::class.java)!!
        assertEquals(pages, meta.pages())
    }

    @Test
    fun `ensure metadata input is preserved when converting to item stack`() {
        val meta = KryptonWrittenBookMeta(CompoundTag.EMPTY)
        val stack = KryptonAdventure.toItemStack(meta)
        assertSame(meta, assertIs(stack.meta))
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun `load translations and factories`() {
            Bootstrapping.loadFactories()
            Bootstrapping.loadRegistries()
        }
    }
}
