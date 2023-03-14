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
