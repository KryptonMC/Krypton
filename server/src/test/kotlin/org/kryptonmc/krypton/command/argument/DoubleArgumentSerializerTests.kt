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

import com.mojang.brigadier.arguments.DoubleArgumentType
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DoubleArgumentSerializerTests : AbstractArgumentSerializerTest() {

    @Test
    fun `ensure double sets no flags when values are double min and max`() {
        val buffer = writeArgumentAndSkipId(DoubleArgumentType.doubleArg())
        assertFlags(buffer, false, false)
    }

    @Test
    fun `ensure double writes no min or max when values are double min and max`() {
        val buffer = writeArgumentAndSkipId(DoubleArgumentType.doubleArg())
        skipFlags(buffer)
        assertNotReadable(buffer)
    }

    @Test
    fun `ensure double sets min and no max flags when max is double max and min is not`() {
        val buffer = writeArgumentAndSkipId(DoubleArgumentType.doubleArg(-1.0))
        assertFlags(buffer, true, false)
    }

    @Test
    fun `ensure double writes min and no max when max is double max and min is not`() {
        val buffer = writeArgumentAndSkipId(DoubleArgumentType.doubleArg(-1.0))
        skipFlags(buffer)
        assertEquals(-1.0, buffer.readDouble())
    }

    @Test
    fun `ensure double sets max and no min flags when min is double min and max is not`() {
        val buffer = writeArgumentAndSkipId(DoubleArgumentType.doubleArg(-Double.MAX_VALUE, 1.0))
        assertFlags(buffer, false, true)
    }

    @Test
    fun `ensure double writes max and no min when min is double min and max is not`() {
        val buffer = writeArgumentAndSkipId(DoubleArgumentType.doubleArg(-Double.MAX_VALUE, 1.0))
        skipFlags(buffer)
        assertEquals(1.0, buffer.readDouble())
    }

    @Test
    fun `ensure double sets min and max flags when min and max are not double min and max`() {
        val buffer = writeArgumentAndSkipId(DoubleArgumentType.doubleArg(-1.0, 1.0))
        assertFlags(buffer, true, true)
    }

    @Test
    fun `ensure double writes min first when min and max are not double min and max`() {
        val buffer = writeArgumentAndSkipId(DoubleArgumentType.doubleArg(-1.0, 1.0))
        skipFlags(buffer)
        assertEquals(-1.0, buffer.readDouble())
    }

    @Test
    fun `ensure double writes max after min when min and max are not double min and max`() {
        val buffer = writeArgumentAndSkipId(DoubleArgumentType.doubleArg(-1.0, 1.0))
        skipFlags(buffer)
        buffer.skipBytes(Double.SIZE_BYTES) // minimum
        assertEquals(1.0, buffer.readDouble())
    }
}
