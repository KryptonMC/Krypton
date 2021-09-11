/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.kryptonmc.api.adventure.toComponent
import org.kryptonmc.api.adventure.toJson
import org.kryptonmc.api.adventure.toJsonString
import org.kryptonmc.api.adventure.toLegacyText
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.api.adventure.toPlainText
import kotlin.test.Test
import kotlin.test.assertEquals

class ComponentTests {

    @Test
    fun `test component to message translation`() {
        val component = Component.text("Hello World!")
        val message = component.toMessage()
        assertEquals("Hello World!", message.string)
        assertEquals(component, message.wrapped)
    }

    @Test
    fun `test component conversions`() {
        val component = Component.text("Hello", NamedTextColor.GREEN)
            .append(Component.text(" World", NamedTextColor.BLUE))
            .append(Component.text("!", NamedTextColor.RED))
        val json = GsonComponentSerializer.gson().serializeToTree(component)
        assertEquals(json, component.toJson())
        assertEquals(GsonComponentSerializer.gson().serialize(component), component.toJsonString())
        assertEquals(PlainTextComponentSerializer.plainText().serialize(component), component.toPlainText())
        assertEquals(
            LegacyComponentSerializer.legacySection().serialize(component),
            component.toLegacyText(LegacyComponentSerializer.SECTION_CHAR)
        )
        assertEquals(
            LegacyComponentSerializer.legacyAmpersand().serialize(component),
            component.toLegacyText(LegacyComponentSerializer.AMPERSAND_CHAR)
        )
        assertEquals(component, json.toComponent())
    }
}
