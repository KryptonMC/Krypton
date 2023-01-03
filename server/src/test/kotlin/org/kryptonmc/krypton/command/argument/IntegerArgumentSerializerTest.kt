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
package org.kryptonmc.krypton.command.argument

import com.mojang.brigadier.arguments.IntegerArgumentType
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class IntegerArgumentSerializerTest : AbstractArgumentSerializerTest() {

    @Test
    fun `ensure int sets no flags when values are int min and max`() {
        val buffer = writeArgumentAndSkipId(IntegerArgumentType.integer())
        assertFlags(buffer, false, false)
    }

    @Test
    fun `ensure int writes no min or max when values are int min and max`() {
        val buffer = writeArgumentAndSkipId(IntegerArgumentType.integer())
        skipFlags(buffer)
        assertNotReadable(buffer)
    }

    @Test
    fun `ensure int sets min and no max flags when max is int max and min is not`() {
        val buffer = writeArgumentAndSkipId(IntegerArgumentType.integer(-1))
        assertFlags(buffer, true, false)
    }

    @Test
    fun `ensure int writes min and no max when max is int max and min is not`() {
        val buffer = writeArgumentAndSkipId(IntegerArgumentType.integer(-1))
        skipFlags(buffer)
        assertEquals(-1, buffer.readInt())
    }

    @Test
    fun `ensure int sets max and no min flags when min is int min and max is not`() {
        val buffer = writeArgumentAndSkipId(IntegerArgumentType.integer(Int.MIN_VALUE, 1))
        assertFlags(buffer, false, true)
    }

    @Test
    fun `ensure int writes max and no min when min is int min and max is not`() {
        val buffer = writeArgumentAndSkipId(IntegerArgumentType.integer(Int.MIN_VALUE, 1))
        skipFlags(buffer)
        assertEquals(1, buffer.readInt())
    }

    @Test
    fun `ensure int sets min and max flags when min and max are not int min and max`() {
        val buffer = writeArgumentAndSkipId(IntegerArgumentType.integer(-1, 1))
        assertFlags(buffer, true, true)
    }

    @Test
    fun `ensure int writes min first when min and max are not int min and max`() {
        val buffer = writeArgumentAndSkipId(IntegerArgumentType.integer(-1, 1))
        skipFlags(buffer)
        assertEquals(-1, buffer.readInt())
    }

    @Test
    fun `ensure int writes max after min when min and max are not int min and max`() {
        val buffer = writeArgumentAndSkipId(IntegerArgumentType.integer(-1, 1))
        skipFlags(buffer)
        buffer.skipBytes(Int.SIZE_BYTES) // minimum
        assertEquals(1, buffer.readInt())
    }
}
