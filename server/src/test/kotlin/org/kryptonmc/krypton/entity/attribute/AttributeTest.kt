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
package org.kryptonmc.krypton.entity.attribute

import org.junit.jupiter.api.Test
import org.kryptonmc.api.entity.attribute.BasicModifierOperation
import org.kryptonmc.api.entity.attribute.ModifierOperation
import java.util.UUID
import kotlin.test.assertEquals

class AttributeTest {

    @Test
    fun `ensure add produces correct result`() {
        assertEquals(9.0, BasicModifierOperation.ADDITION.apply(BASE, MODIFIERS))
    }

    @Test
    fun `ensure multiply base produces correct result`() {
        assertEquals(21.0, BasicModifierOperation.MULTIPLY_BASE.apply(BASE, MODIFIERS))
    }

    @Test
    fun `ensure multiply total produces correct result`() {
        assertEquals(45.0, BasicModifierOperation.MULTIPLY_TOTAL.apply(BASE, MODIFIERS))
    }

    companion object {

        private const val BASE = 3.0
        private val OPERATION = ModifierOperation { _, _ -> 0.0 }
        private val MODIFIERS = setOf(
            KryptonAttributeModifier(UUID.randomUUID(), "1", 2.0, OPERATION),
            KryptonAttributeModifier(UUID.randomUUID(), "2", 4.0, OPERATION)
        )
    }
}
