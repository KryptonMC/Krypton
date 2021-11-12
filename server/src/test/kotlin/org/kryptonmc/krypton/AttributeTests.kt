/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton

import org.kryptonmc.api.entity.attribute.ModifierOperation
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeModifier
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class AttributeTests {

    @Test
    fun add() {
        assertEquals(9.0, ModifierOperation.ADD.apply(BASE, MODIFIERS))
    }

    @Test
    fun `multiply base`() {
        assertEquals(21.0, ModifierOperation.MULTIPLY_BASE.apply(BASE, MODIFIERS))
    }

    @Test
    fun `multiply total`() {
        assertEquals(45.0, ModifierOperation.MULTIPLY_TOTAL.apply(BASE, MODIFIERS))
    }

    companion object {

        const val BASE = 3.0
        val MODIFIERS = setOf(
            KryptonAttributeModifier("1", UUID.randomUUID(), 2.0),
            KryptonAttributeModifier("2", UUID.randomUUID(), 4.0)
        )
    }
}
