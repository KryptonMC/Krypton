/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.inventory.item.dsl.item
import kotlin.test.Test
import kotlin.test.assertEquals

class InventoryTests {

    @Test
    fun `test inventory type to string`() {
        val type = InventoryType.ANVIL
        assertEquals(type.toString(), "anvil")
    }

    @Test
    fun `test invalid item builder values`() {
        assertThrows<java.lang.IllegalArgumentException> { item { amount(128) } }
        assertThrows<IllegalArgumentException> { item { amount(65) } }
        assertDoesNotThrow { item { amount(64) } }
        assertDoesNotThrow { item { amount(0) } }
        assertThrows<IllegalArgumentException> { item { amount(-1) } }
    }
}
