/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.krypton.api

import io.mockk.every
import io.mockk.mockk
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.krypton.api.inventory.InventoryType
import org.kryptonmc.krypton.api.inventory.item.ItemStack
import org.kryptonmc.krypton.api.inventory.item.Material
import org.kryptonmc.krypton.api.inventory.item.dsl.item
import org.kryptonmc.krypton.api.inventory.item.meta.ItemMeta
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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
            every { displayName } returns text("Nothing")
            every { lore } returns listOf(text("Lorem ipsum"), text("dolor sit amet"))
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

    @Test
    fun `motion blocking persistence`() {
        assertTrue(Material.ACACIA_BOAT.blocksMotion)
        assertFalse(Material.AIR.blocksMotion)
    }

    @Test
    fun `built item metadata persistence`() {
        val name = text("Hello World!")
        val lore = listOf(text("Lorem ipsum"), text("dolor sit amet"))
        val item = item(Material.AIR, 1) {
            name(name)
            lore(lore)
        }

        assertEquals(name, item.meta?.displayName)
        assertEquals(lore, item.meta?.lore)
    }
}
