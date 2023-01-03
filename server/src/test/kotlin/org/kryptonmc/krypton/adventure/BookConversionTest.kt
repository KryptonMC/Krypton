/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
