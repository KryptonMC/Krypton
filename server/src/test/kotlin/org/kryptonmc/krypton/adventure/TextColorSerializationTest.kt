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

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.adventure.network.TextColorSerialization
import org.kryptonmc.krypton.commands.krypton.KryptonColors
import java.util.Locale
import kotlin.test.assertEquals

class TextColorSerializationTest {

    @Test
    fun `ensure unnamed colour encode produces hex string`() {
        assertEquals(asHexString(KryptonColors.STANDARD_PURPLE), TextColorSerialization.encode(KryptonColors.STANDARD_PURPLE))
    }

    @Test
    fun `ensure named colour encode produces name`() {
        assertEquals(NamedTextColor.WHITE.toString(), TextColorSerialization.encode(NamedTextColor.WHITE))
    }

    @Test
    fun `ensure name decode produces named colour`() {
        assertEquals(NamedTextColor.WHITE, TextColorSerialization.decode(NamedTextColor.WHITE.toString()))
    }

    @Test
    fun `ensure text colour hex decode produces unnamed colour with correct value`() {
        assertEquals(KryptonColors.STANDARD_PURPLE, TextColorSerialization.decode(asHexString(KryptonColors.STANDARD_PURPLE)))
    }

    companion object {

        @JvmStatic
        private fun asHexString(color: TextColor): String = String.format(Locale.ROOT, "#%06X", color.value())
    }
}
