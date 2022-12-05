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
package org.kryptonmc.krypton.entity.attribute

import org.junit.jupiter.api.Test
import org.kryptonmc.api.entity.attribute.BasicModifierOperation
import org.kryptonmc.api.entity.attribute.ModifierOperation
import java.util.UUID
import kotlin.test.assertEquals

class AttributeTests {

    @Test
    fun add() {
        assertEquals(9.0, BasicModifierOperation.ADDITION.apply(BASE, MODIFIERS))
    }

    @Test
    fun `multiply base`() {
        assertEquals(21.0, BasicModifierOperation.MULTIPLY_BASE.apply(BASE, MODIFIERS))
    }

    @Test
    fun `multiply total`() {
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
