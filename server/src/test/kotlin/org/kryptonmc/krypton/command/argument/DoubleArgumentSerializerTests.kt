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
        assertExhausted(buffer)
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
        assertEquals(-1.0, buffer.double)
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
        assertEquals(1.0, buffer.double)
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
        assertEquals(-1.0, buffer.double)
    }

    @Test
    fun `ensure double writes max after min when min and max are not double min and max`() {
        val buffer = writeArgumentAndSkipId(DoubleArgumentType.doubleArg(-1.0, 1.0))
        skipFlags(buffer)
        buffer.position(buffer.position() + Double.SIZE_BYTES) // minimum
        assertEquals(1.0, buffer.double)
    }
}
