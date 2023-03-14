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
