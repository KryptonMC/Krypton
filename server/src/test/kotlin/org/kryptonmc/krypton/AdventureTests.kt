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
package org.kryptonmc.krypton

import net.kyori.adventure.inventory.Book
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.api.BinaryTagHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.util.Codec
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.api.adventure.toJson
import org.kryptonmc.api.adventure.toPlainText
import org.kryptonmc.api.item.meta
import org.kryptonmc.api.item.meta.WrittenBookMeta
import org.kryptonmc.krypton.adventure.KryptonAdventure
import org.kryptonmc.krypton.adventure.id
import org.kryptonmc.krypton.adventure.toItemStack
import org.kryptonmc.krypton.adventure.NBTLegacyHoverEventSerializer
import org.kryptonmc.krypton.util.Bootstrap
import org.kryptonmc.krypton.util.Reflection
import java.io.IOException
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class AdventureTests {

    @Test
    fun `test flattening of translatable components`() {
        KryptonAdventure.FLATTENER.flatten(Component.translatable("language.name")) {
            assertEquals("English", it)
        }
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `ensure ids do not break on update`() {
        val values = Reflection.accessField<NamedTextColor, List<NamedTextColor>>("VALUES")!!
        values.forEachIndexed { index, element -> assertEquals(index, element.id()) }
    }

    @Test
    fun `test item stack conversion`() {
        val title = Component.text("title")
        val author = Component.text("author")
        val pages = listOf(Component.text("Page 1"), Component.text("Page 2"), Component.text("Page 3"))
        val meta = Book.book(title, author, pages).toItemStack().meta<WrittenBookMeta>()!!
        assertEquals("title", meta.title.toPlainText())
        assertEquals("author", meta.author.toPlainText())
        assertEquals(pages, meta.pages)
    }

    @Test
    fun `test show item deserialization`() {
        // Standard
        val input = Component.text("{Count:37B,id:'minecraft:air',tag:{Damage:34,Unbreakable:1B,CustomModelData:74}}")
        val item = HoverEvent.ShowItem.of(
            Key.key("air"),
            37,
            BinaryTagHolder.of("{CustomModelData:74,Damage:34,Unbreakable:1b}")
        )
        assertEquals(item, NBTLegacyHoverEventSerializer.deserializeShowItem(input))

        // No extra item data (`tag` in item NBT)
        val noTag = Component.text("{Count:37B,id:'minecraft:air'}")
        assertEquals(item.nbt(null), NBTLegacyHoverEventSerializer.deserializeShowItem(noTag))
    }

    @Test
    fun `test show item serialization`() {
        // Standard
        val item = HoverEvent.ShowItem.of(
            Key.key("air"),
            37,
            BinaryTagHolder.of("{CustomModelData:74,Damage:34,Unbreakable:1b}")
        )
        val output = Component.text("{Count:37b,id:\"minecraft:air\",tag:{CustomModelData:74,Damage:34,Unbreakable:1b}}")
        assertEquals(output, NBTLegacyHoverEventSerializer.serializeShowItem(item))

        // No extra item data (`tag` in item NBT)
        val noTag = Component.text("{Count:37b,id:\"minecraft:air\"}")
        assertEquals(noTag, NBTLegacyHoverEventSerializer.serializeShowItem(item.nbt(null)))

        // Bogus NBT
        assertThrows<IOException> { NBTLegacyHoverEventSerializer.serializeShowItem(item.nbt(BinaryTagHolder.of("hello world"))) }
    }

    @Test
    fun `test show entity deserialization`() {
        val uuid = UUID.fromString("aaaabbbb-cccc-dddd-eeee-ffffaaaabbbb")
        val name = Component.text("What a name")

        // Standard
        val input = Component.text("{type:'minecraft:pig',id:'$uuid',name:'${name.toJson()}'}")
        val entity = HoverEvent.ShowEntity.of(Key.key("pig"), uuid, name)
        assertEquals(entity, NBTLegacyHoverEventSerializer.deserializeShowEntity(input, DECODER))

        // Bogus NBT
        val bogus = Component.text("hello world")
        assertThrows<IOException> { NBTLegacyHoverEventSerializer.deserializeShowEntity(bogus, DECODER) }
    }

    @Test
    fun `test show entity serialization`() {
        val uuid = UUID.fromString("aaaabbbb-cccc-dddd-eeee-ffffaaaabbbb")
        val name = Component.text("What a name")

        // Standard
        val output = Component.text("{id:\"$uuid\",name:'${name.toJson()}',type:\"minecraft:pig\"}")
        val entity = HoverEvent.ShowEntity.of(Key.key("pig"), uuid, name)
        assertEquals(output, NBTLegacyHoverEventSerializer.serializeShowEntity(entity, ENCODER))
    }

    companion object {

        private val ENCODER = Codec.Encoder<Component, String, RuntimeException>(GsonComponentSerializer.gson()::serialize)
        private val DECODER = Codec.Decoder<Component, String, RuntimeException>(GsonComponentSerializer.gson()::deserialize)

        @JvmStatic
        @BeforeAll
        fun `preload bootstrap`() {
            Bootstrap.preload()
        }
    }
}
