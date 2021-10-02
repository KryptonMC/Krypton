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
import org.kryptonmc.api.util.Catalogue

/**
 * All the built-in inventory types.
 *
 * Note: The player inventory is not included in this list as it is not a valid
 * menu type, and cannot be created, or opened client-side.
 */
@Suppress("UndocumentedPublicProperty")
@Catalogue(InventoryType::class, GridInventoryType::class)
public object InventoryTypes {

    // @formatter:off
    @JvmField public val GENERIC_9x1: GridInventoryType = register("generic_9x1", 9, 1)
    @JvmField public val GENERIC_9x2: GridInventoryType = register("generic_9x2", 9, 2)
    @JvmField public val GENERIC_9x3: GridInventoryType = register("generic_9x3", 9, 3)
    @JvmField public val GENERIC_9x4: GridInventoryType = register("generic_9x4", 9, 4)
    @JvmField public val GENERIC_9x5: GridInventoryType = register("generic_9x5", 9, 5)
    @JvmField public val GENERIC_9x6: GridInventoryType = register("generic_9x6", 9, 6)
    @JvmField public val GENERIC_3x3: GridInventoryType = register("generic_3x3", 9, 3)
    @JvmField public val ANVIL: InventoryType = register("anvil", 3)
    @JvmField public val BEACON: InventoryType = register("beacon", 1)
    @JvmField public val BLAST_FURNACE: InventoryType = register("blast_furnace", 3)
    @JvmField public val BREWING_STAND: InventoryType = register("brewing_stand", 5)
    @JvmField public val CRAFTING_TABLE: InventoryType = register("crafting", 10)
    @JvmField public val ENCHANTING_TABLE: InventoryType = register("enchantment", 2)
    @JvmField public val FURNACE: InventoryType = register("furnace", 3)
    @JvmField public val GRINDSTONE: InventoryType = register("grindstone", 3)
    @JvmField public val HOPPER: GridInventoryType = register("hopper", 5, 1)
    @JvmField public val LECTERN: InventoryType = register("lectern", 1)
    @JvmField public val LOOM: InventoryType = register("loom", 4)
    @JvmField public val MERCHANT: InventoryType = register("merchant", 3)
    @JvmField public val SHULKER_BOX: GridInventoryType = register("shulker_box", 9, 3)
    @JvmField public val SMITHING_TABLE: InventoryType = register("smithing", 3)
    @JvmField public val SMOKER: InventoryType = register("smoker", 3)
    @JvmField public val CARTOGRAPHY_TABLE: InventoryType = register("cartography_table", 3)
    @JvmField public val STONECUTTER: InventoryType = register("stonecutter", 2)

    // @formatter:on
    @JvmStatic
    private fun register(name: String, size: Int): InventoryType {
        val key = Key.key(name)
        return Registries.register(Registries.MENU, key, InventoryType.of(key, size))
    }

    @JvmStatic
    private fun register(name: String, columns: Int, rows: Int): GridInventoryType {
        val key = Key.key(name)
        return Registries.register(Registries.MENU, key, GridInventoryType.of(key, rows, columns)) as GridInventoryType
    }
}
