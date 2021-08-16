/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.ModifierOperation
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class AttributeTests {

    @Test
    fun `add modifier`() {
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
        val MODIFIERS = listOf(
            AttributeModifier("1", UUID.randomUUID(), 2.0),
            AttributeModifier("2", UUID.randomUUID(), 4.0)
        )
    }
}
