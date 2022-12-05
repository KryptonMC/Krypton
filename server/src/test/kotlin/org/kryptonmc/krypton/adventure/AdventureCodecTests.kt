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
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.commands.krypton.KryptonColors
import org.kryptonmc.krypton.testutil.Bootstrapping
import org.kryptonmc.serialization.gson.GsonOps
import java.util.Locale
import kotlin.test.assertEquals

class AdventureCodecTests {

    @Test
    fun `test text colour serialization`() {
        assertEquals(NamedTextColor.WHITE.toString(), TextColorSerialization.encode(NamedTextColor.WHITE))
        assertEquals(asHexString(KryptonColors.STANDARD_PURPLE), TextColorSerialization.encode(KryptonColors.STANDARD_PURPLE))
    }

    @Test
    fun `test text colour deserialization`() {
        assertEquals(NamedTextColor.WHITE, TextColorSerialization.decode(NamedTextColor.WHITE.toString()))
        assertEquals(KryptonColors.STANDARD_PURPLE, TextColorSerialization.decode(asHexString(KryptonColors.STANDARD_PURPLE)))
    }

    @Test
    fun `test style serialization`() {
        // We use Adventure's internal Style serializer here because we assume that always produces a correct result for this test,
        // and so we can use the result as a reference for testing our own serializer.
        val jsonSerializer = GsonComponentSerializer.gson().serializer()
        val codec = AdventureCodecs.STYLE_FORMATTING

        val simple = Style.style(NamedTextColor.WHITE)
        val complex = Style.style()
            .color(NamedTextColor.RED)
            .decorate(TextDecoration.BOLD)
            .decorate(TextDecoration.UNDERLINED)
            .decorate(TextDecoration.ITALIC)
            .insertion("Hello World!")
            .font(Key.key("krypton", "do_things"))
            .build()
        val simpleJson = jsonSerializer.toJsonTree(simple, Style::class.java)
        val complexJson = jsonSerializer.toJsonTree(complex, Style::class.java)
        assertEquals(simple, codec.read(simpleJson, GsonOps.INSTANCE).result().orElseThrow())
        assertEquals(complex, codec.read(complexJson, GsonOps.INSTANCE).result().orElseThrow())
    }

    @Test
    fun `test style deserialization`() {
        // We use Adventure's internal Style serializer here because we assume that always produces a correct result for this test,
        // and so we can use the result as a reference for testing our own serializer.
        val jsonSerializer = GsonComponentSerializer.gson().serializer()
        val codec = AdventureCodecs.STYLE_FORMATTING

        val simple = Style.style(NamedTextColor.WHITE)
        val complex = Style.style()
            .color(NamedTextColor.RED)
            .decorate(TextDecoration.BOLD)
            .decorate(TextDecoration.UNDERLINED)
            .decorate(TextDecoration.ITALIC)
            .insertion("Hello World!")
            .font(Key.key("krypton", "do_things"))
            .build()
        val simpleJson = codec.encodeStart(simple, GsonOps.INSTANCE).result().orElseThrow()
        val complexJson = codec.encodeStart(complex, GsonOps.INSTANCE).result().orElseThrow()
        assertEquals(simple, jsonSerializer.fromJson(simpleJson, Style::class.java))
        assertEquals(complex, jsonSerializer.fromJson(complexJson, Style::class.java))
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun `load factories for color`() {
            Bootstrapping.loadFactories()
        }

        // TODO: Remove when Adventure is updated to 4.12.0
        @JvmStatic
        private fun asHexString(color: TextColor): String = String.format(Locale.ROOT, "#%06X", color.value())
    }
}
