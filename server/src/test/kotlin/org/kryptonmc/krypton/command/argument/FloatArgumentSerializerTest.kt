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

import com.mojang.brigadier.arguments.FloatArgumentType
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FloatArgumentSerializerTest : AbstractArgumentSerializerTest() {

    @Test
    fun `ensure float sets no flags when values are float min and float max`() {
        val buffer = writeArgumentAndSkipId(FloatArgumentType.floatArg())
        assertFlags(buffer, false, false)
    }

    @Test
    fun `ensure float writes no min or max when values are float min and float max`() {
        val buffer = writeArgumentAndSkipId(FloatArgumentType.floatArg())
        skipFlags(buffer)
        assertNotReadable(buffer)
    }

    @Test
    fun `ensure float sets min and no max flags when max is float max and min is not`() {
        val buffer = writeArgumentAndSkipId(FloatArgumentType.floatArg(-1F))
        assertFlags(buffer, true, false)
    }

    @Test
    fun `ensure float writes min and no max when max is float max and min is not`() {
        val buffer = writeArgumentAndSkipId(FloatArgumentType.floatArg(-1F))
        skipFlags(buffer)
        assertEquals(-1F, buffer.readFloat())
    }

    @Test
    fun `ensure float sets max and no min flags when min is float min and max is not`() {
        val buffer = writeArgumentAndSkipId(FloatArgumentType.floatArg(-Float.MAX_VALUE, 1F))
        assertFlags(buffer, false, true)
    }

    @Test
    fun `ensure float writes max and no min when min is float min and max is not`() {
        val buffer = writeArgumentAndSkipId(FloatArgumentType.floatArg(-Float.MAX_VALUE, 1F))
        skipFlags(buffer)
        assertEquals(1F, buffer.readFloat())
    }

    @Test
    fun `ensure float sets min and max flags when min and max are not float min and max`() {
        val buffer = writeArgumentAndSkipId(FloatArgumentType.floatArg(-1F, 1F))
        assertFlags(buffer, true, true)
    }

    @Test
    fun `ensure float writes min first when min and max are not float min and max`() {
        val buffer = writeArgumentAndSkipId(FloatArgumentType.floatArg(-1F, 1F))
        skipFlags(buffer)
        assertEquals(-1F, buffer.readFloat())
    }

    @Test
    fun `ensure float writes max after min when min and max are not float min and max`() {
        val buffer = writeArgumentAndSkipId(FloatArgumentType.floatArg(-1F, 1F))
        skipFlags(buffer)
        buffer.skipBytes(Float.SIZE_BYTES) // minimum
        assertEquals(1F, buffer.readFloat())
    }
}
