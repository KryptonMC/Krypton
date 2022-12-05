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
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.api.BinaryTagHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.util.Codec
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.api.item.meta.WrittenBookMeta
import org.kryptonmc.krypton.commands.krypton.KryptonColors
import org.kryptonmc.krypton.item.meta.KryptonWrittenBookMeta
import org.kryptonmc.krypton.testutil.Bootstrapping
import org.kryptonmc.krypton.util.Reflection
import org.kryptonmc.nbt.CompoundTag
import java.io.IOException
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class AdventureTests {

    @Test
    fun `test flattening of translatable components`() {
        KryptonAdventure.FLATTENER.flatten(Component.translatable("language.name")) { assertEquals("English", it) }
    }

    @Test
    fun `ensure ids do not break on update`() {
        val values = Reflection.accessField<NamedTextColor, List<NamedTextColor>>("VALUES")!!
        values.forEachIndexed { index, element -> assertEquals(index, KryptonAdventure.colorId(element)) }
    }

    @Test
    fun `test item stack conversion`() {
        val title = Component.text("title")
        val author = Component.text("author")
        val pages = listOf(Component.text("Page 1"), Component.text("Page 2"), Component.text("Page 3"))
        val meta = KryptonAdventure.toItemStack(Book.book(title, author, pages)).meta(WrittenBookMeta::class.java)!!
        assertEquals("title", meta.title().toPlainText())
        assertEquals("author", meta.author().toPlainText())
        assertEquals(pages, meta.pages())
    }

    @Test
    fun `test item stack conversion with meta input`() {
        val meta = KryptonWrittenBookMeta(CompoundTag.EMPTY)
        val stack = KryptonAdventure.toItemStack(meta)
        assertSame(meta, assertIs(stack.meta))
    }

    @Test
    fun `test show item deserialization`() {
        // Standard
        val nbt = "{CustomModelData:74,Damage:34,Unbreakable:1b}"
        val input = Component.text("{Count:37B,id:'minecraft:air',tag:$nbt}")
        val item = HoverEvent.ShowItem.of(Key.key("air"), 37, BinaryTagHolder.binaryTagHolder(nbt))
        assertEquals(item, NBTLegacyHoverEventSerializer.deserializeShowItem(input))

        // No extra item data (`tag` in item NBT)
        val noTag = Component.text("{Count:37B,id:'minecraft:air'}")
        assertEquals(item.nbt(null), NBTLegacyHoverEventSerializer.deserializeShowItem(noTag))
    }

    @Test
    fun `test show item serialization`() {
        // Standard
        val nbt = "{CustomModelData:74,Damage:34,Unbreakable:1b}"
        val item = HoverEvent.ShowItem.of(Key.key("air"), 37, BinaryTagHolder.binaryTagHolder(nbt))
        val output = Component.text("{id:\"minecraft:air\",Count:37b,tag:$nbt}")
        assertEquals(output, NBTLegacyHoverEventSerializer.serializeShowItem(item))

        // No extra item data (`tag` in item NBT)
        val noTag = Component.text("{id:\"minecraft:air\",Count:37b}")
        assertEquals(noTag, NBTLegacyHoverEventSerializer.serializeShowItem(item.nbt(null)))

        // Bogus NBT
        assertThrows<IOException> { NBTLegacyHoverEventSerializer.serializeShowItem(item.nbt(BinaryTagHolder.binaryTagHolder("hello world"))) }
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
        val output = Component.text("{id:\"$uuid\",type:\"minecraft:pig\",name:'${name.toJson()}'}")
        val entity = HoverEvent.ShowEntity.of(Key.key("pig"), uuid, name)
        assertEquals(output, NBTLegacyHoverEventSerializer.serializeShowEntity(entity, ENCODER))
    }

    @Test
    fun `test translatable flattening`() {
        var called = false
        KryptonAdventure.FLATTENER.flatten(Component.translatable("language.name")) {
            called = true
            assertEquals("English", it)
        }
        assertTrue(called, "Flattener was not called!")
    }

    @Test
    fun `ensure stable json outputs identical json for same component`() {
        val simple = Component.text("Hello World!")
        val complex = Component.text()
            .content("Hello World!")
            .color(KryptonColors.STANDARD_PURPLE)
            .decorate(TextDecoration.BOLD)
            .decorate(TextDecoration.STRIKETHROUGH)
            .decorate(TextDecoration.OBFUSCATED)
            .clickEvent(ClickEvent.suggestCommand("/hello_world"))
            .hoverEvent(HoverEvent.showEntity(HoverEvent.ShowEntity.of(Key.key("pig"), UUID.randomUUID(), Component.text("The most awesome pig"))))
            .insertion("I am a stable complex component!")
            .appendSpace()
            .appendNewline()
            .appendNewline()
            .append(Component.text("Goodbye World!"))
            .font(Key.key("krypton", "awesome_test_font"))
            .build()
        val simpleStable = KryptonAdventure.toStableJson(simple)
        val complexStable = KryptonAdventure.toStableJson(complex)
        for (i in 0 until 10) {
            assertEquals(simpleStable, KryptonAdventure.toStableJson(simple))
            assertEquals(complexStable, KryptonAdventure.toStableJson(complex))
        }
    }

    companion object {

        private val ENCODER = Codec.Encoder<Component, String, RuntimeException>(GsonComponentSerializer.gson()::serialize)
        private val DECODER = Codec.Decoder<Component, String, RuntimeException>(GsonComponentSerializer.gson()::deserialize)

        @JvmStatic
        @BeforeAll
        fun `load translations and factories`() {
            Bootstrapping.loadTranslations()
            Bootstrapping.loadFactories()
            Bootstrapping.loadRegistries()
        }
    }
}
