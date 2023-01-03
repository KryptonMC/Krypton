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
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.commands.krypton.KryptonColors
import java.util.UUID
import kotlin.test.assertEquals

class ComponentJsonTest {

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
        @Suppress("UnusedPrivateMember")
        for (i in 0 until 10) {
            assertEquals(simpleStable, KryptonAdventure.toStableJson(simple))
            assertEquals(complexStable, KryptonAdventure.toStableJson(complex))
        }
    }
}
