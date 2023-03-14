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
