package org.kryptonmc.krypton.api

import io.mockk.every
import io.mockk.mockk
import net.kyori.adventure.text.Component
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.krypton.api.inventory.InventoryType
import org.kryptonmc.krypton.api.inventory.item.ItemStack
import org.kryptonmc.krypton.api.inventory.item.Material
import org.kryptonmc.krypton.api.inventory.item.dsl.item
import org.kryptonmc.krypton.api.inventory.item.meta.ItemMeta
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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

    @Test
    fun `test inventory type size and title retention`() {
        val type = InventoryType.ANVIL
        assertEquals(3, type.size)
        assertEquals("Repairing", type.title)
    }

    @Test
    fun `test item stack data retention`() {
        val meta = mockk<ItemMeta> {
            every { displayName } returns Component.text("Nothing")
            every { lore } returns listOf(Component.text("Lorem ipsum"), Component.text("dolor sit amet"))
        }
        val stack = ItemStack(Material.AIR, 10, meta)

        assertEquals(Material.AIR, stack.type)
        assertEquals(10, stack.amount)
        assertEquals(meta, stack.meta)
    }

    @Test
    fun `test material index is not null`() {
        assertNotNull(Material.KEYS)
        assertNotNull(Material.KEYS.keys())
        assertNotNull(Material.KEYS.values())
    }
}
