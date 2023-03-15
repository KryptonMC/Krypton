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
        assertExhausted(buffer)
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
        assertEquals(-1F, buffer.float)
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
        assertEquals(1F, buffer.float)
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
        assertEquals(-1F, buffer.float)
    }

    @Test
    fun `ensure float writes max after min when min and max are not float min and max`() {
        val buffer = writeArgumentAndSkipId(FloatArgumentType.floatArg(-1F, 1F))
        skipFlags(buffer)
        buffer.position(buffer.position() + Float.SIZE_BYTES) // minimum
        assertEquals(1F, buffer.float)
    }
}
