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

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.kryptonmc.krypton.api.InitializerExtension
import org.kryptonmc.krypton.api.Initializers
import org.kryptonmc.krypton.api.FactoryProviderInitializer
import org.kryptonmc.krypton.util.serialization.encode
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.compound
import java.util.EnumSet
import java.util.Locale
import java.util.concurrent.ThreadLocalRandom
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(InitializerExtension::class)
@Initializers(FactoryProviderInitializer::class)
class AdventureCodecTests {

    @Test
    fun `test style formatting codec`() {
        val full = Style.style()
            .color(NamedTextColor.DARK_GRAY)
            .decorate(TextDecoration.BOLD)
            .decorate(TextDecoration.ITALIC)
            .decorate(TextDecoration.UNDERLINED)
            .decorate(TextDecoration.STRIKETHROUGH)
            .decorate(TextDecoration.OBFUSCATED)
            .insertion("test")
            .font(Key.key("krypton", "font"))
            .build()
        val noDecorations = full.toBuilder()
            .decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET)
            .decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET)
            .decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET)
            .decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET)
            .build()
        val falseDecorations = full.decorations(EnumSet.allOf(TextDecoration::class.java), false)
        val fullTag = compound {
            encode(AdventureCodecs.TEXT_COLOR, "color", NamedTextColor.DARK_GRAY)
            boolean("bold", true)
            boolean("italic", true)
            boolean("underlined", true)
            boolean("strikethrough", true)
            boolean("obfuscated", true)
            string("insertion", "test")
            string("font", "krypton:font")
        }
        val noDecorationsTag = compound {
            encode(AdventureCodecs.TEXT_COLOR, "color", NamedTextColor.DARK_GRAY)
            string("insertion", "test")
            string("font", "krypton:font")
        }
        val falseDecorationsTag = compound {
            encode(AdventureCodecs.TEXT_COLOR, "color", NamedTextColor.DARK_GRAY)
            boolean("bold", false)
            boolean("italic", false)
            boolean("underlined", false)
            boolean("strikethrough", false)
            boolean("obfuscated", false)
            string("insertion", "test")
            string("font", "krypton:font")
        }
        assertEquals(fullTag, AdventureCodecs.STYLE_FORMATTING.encode(full))
        assertEquals(full, AdventureCodecs.STYLE_FORMATTING.decode(fullTag))
        assertEquals(noDecorationsTag, AdventureCodecs.STYLE_FORMATTING.encode(noDecorations))
        assertEquals(noDecorations, AdventureCodecs.STYLE_FORMATTING.decode(noDecorationsTag))
        assertEquals(falseDecorationsTag, AdventureCodecs.STYLE_FORMATTING.encode(falseDecorations))
        assertEquals(falseDecorations, AdventureCodecs.STYLE_FORMATTING.decode(falseDecorationsTag))
    }

    @Test
    fun `test text color codec`() {
        val color = TextColor.color(ThreadLocalRandom.current().nextInt(16777215))
        val encodedNamed = StringTag.of(NamedTextColor.NAMES.key(NamedTextColor.DARK_GRAY)!!)
        val encodedColor = StringTag.of(String.format(Locale.ROOT, "#%06X", color.value()))
        assertEquals(encodedNamed, AdventureCodecs.TEXT_COLOR.encode(NamedTextColor.DARK_GRAY))
        assertEquals(encodedColor, AdventureCodecs.TEXT_COLOR.encode(color))
        assertEquals(NamedTextColor.DARK_GRAY, AdventureCodecs.TEXT_COLOR.decode(encodedNamed))
        assertEquals(color, AdventureCodecs.TEXT_COLOR.decode(encodedColor))
        assertThrows<IllegalArgumentException> { AdventureCodecs.TEXT_COLOR.decode(StringTag.of(color.value().toString())) }
        assertThrows<IllegalArgumentException> { AdventureCodecs.TEXT_COLOR.decode(StringTag.of("fadggsavdsgdsagdg")) }
    }
}
