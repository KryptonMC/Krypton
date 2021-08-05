/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.inventory

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries

/**
 * All the built-in inventory types.
 *
 * Note: The player inventory is not included in this list as it is not a valid
 * menu type, and cannot be created, or opened client-side.
 */
object InventoryTypes {

    // @formatter:off
    @JvmField val GENERIC_9x1 = register("generic_9x1", 9, 1)
    @JvmField val GENERIC_9x2 = register("generic_9x2", 9, 2)
    @JvmField val GENERIC_9x3 = register("generic_9x3", 9, 3)
    @JvmField val GENERIC_9x4 = register("generic_9x4", 9, 4)
    @JvmField val GENERIC_9x5 = register("generic_9x5", 9, 5)
    @JvmField val GENERIC_9x6 = register("generic_9x6", 9, 6)
    @JvmField val GENERIC_3x3 = register("generic_3x3", 9, 3)
    @JvmField val ANVIL = register("anvil", 3)
    @JvmField val BEACON = register("beacon", 1)
    @JvmField val BLAST_FURNACE = register("blast_furnace", 3)
    @JvmField val BREWING_STAND = register("brewing_stand", 5)
    @JvmField val CRAFTING_TABLE = register("crafting", 10)
    @JvmField val ENCHANTING_TABLE = register("enchantment", 2)
    @JvmField val FURNACE = register("furnace", 3)
    @JvmField val GRINDSTONE = register("grindstone", 3)
    @JvmField val HOPPER = register("hopper", 5, 1)
    @JvmField val LECTERN = register("lectern", 1)
    @JvmField val LOOM = register("loom", 4)
    @JvmField val MERCHANT = register("merchant", 3)
    @JvmField val SHULKER_BOX = register("shulker_box", 9, 3)
    @JvmField val SMITHING_TABLE = register("smithing", 3)
    @JvmField val SMOKER = register("smoker", 3)
    @JvmField val CARTOGRAPHY_TABLE = register("cartography_table", 3)
    @JvmField val STONECUTTER = register("stonecutter", 2)

    // @formatter:on
    private fun register(name: String, size: Int): InventoryType {
        val key = Key.key(name)
        return Registries.register(Registries.MENU, key, InventoryType(key, size))
    }

    private fun register(name: String, columns: Int, rows: Int): GridInventoryType {
        val key = Key.key(name)
        return Registries.register(Registries.MENU, key, GridInventoryType(key, rows, columns)) as GridInventoryType
    }
}
