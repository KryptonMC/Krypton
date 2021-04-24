package org.kryptonmc.krypton.api

import org.junit.jupiter.api.assertThrows
import org.kryptonmc.krypton.api.inventory.InventoryType
import org.kryptonmc.krypton.api.inventory.item.dsl.item
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
        assertThrows<IllegalArgumentException> {
            item { amount(128) }
        }
    }
}
