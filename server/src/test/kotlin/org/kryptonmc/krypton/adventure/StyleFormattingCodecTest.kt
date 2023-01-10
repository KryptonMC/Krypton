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
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.adventure.network.AdventureCodecs
import org.kryptonmc.serialization.gson.GsonOps
import kotlin.test.assertEquals

/*
 * In these tests, we use Adventure's internal Style serializer, produced from `GsonComponentSerializer.gson().serializer()`, to verify our
 * codec's output, as we should be able to safely assume that this serializer is correct.
 *
 * We could consider using vanilla's serializer instead in future, however, that would require conversion between vanilla's and
 * Adventure's components, which is less than ideal.
 *
 * Note: A "simple" style here is defined with one that only has colour, as colour is the most common styling element to be set for components.
 * The complex style has all formatting elements with values set, which includes colour, decorations, insertion, and font.
 * Events were emitted from these tests as the codec does not support them, since they are not considered formatting elements.
 */
class StyleFormattingCodecTest {

    @Test
    fun `ensure simple style serializes to correct json`() {
        val simple = Style.style(NamedTextColor.WHITE)
        val simpleJson = GsonComponentSerializer.gson().serializer().toJsonTree(simple, Style::class.java)
        assertEquals(simple, AdventureCodecs.STYLE_FORMATTING.read(simpleJson, GsonOps.INSTANCE).result().orElseThrow())
    }

    @Test
    fun `ensure complex style serializes to correct json`() {
        val complex = Style.style()
            .color(NamedTextColor.RED)
            .decorate(TextDecoration.BOLD)
            .decorate(TextDecoration.UNDERLINED)
            .decorate(TextDecoration.ITALIC)
            .insertion("Hello World!")
            .font(Key.key("krypton", "cryptic"))
            .build()
        val complexJson = GsonComponentSerializer.gson().serializer().toJsonTree(complex, Style::class.java)
        assertEquals(complex, AdventureCodecs.STYLE_FORMATTING.read(complexJson, GsonOps.INSTANCE).result().orElseThrow())
    }

    @Test
    fun `ensure simple style produces correct json`() {
        val simple = Style.style(NamedTextColor.WHITE)
        val simpleJson = GsonComponentSerializer.gson().serializer().toJsonTree(simple, Style::class.java)
        assertEquals(simpleJson, AdventureCodecs.STYLE_FORMATTING.encodeStart(simple, GsonOps.INSTANCE).result().orElseThrow())
    }

    @Test
    fun `ensure complex style produces correct json`() {
        val complex = Style.style()
            .color(NamedTextColor.RED)
            .decorate(TextDecoration.BOLD)
            .decorate(TextDecoration.UNDERLINED)
            .decorate(TextDecoration.ITALIC)
            .insertion("Hello World!")
            .font(Key.key("krypton", "cryptic"))
            .build()
        val complexJson = GsonComponentSerializer.gson().serializer().toJsonTree(complex, Style::class.java)
        assertEquals(complexJson, AdventureCodecs.STYLE_FORMATTING.encodeStart(complex, GsonOps.INSTANCE).result().orElseThrow())
    }
}
