/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.inventory

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All the built-in inventory types.
 *
 * Note: The player inventory is not included in this list as it is not a valid
 * menu type, and cannot be created, or opened client-side.
 */
@Catalogue(InventoryType::class)
public object InventoryTypes {

    // @formatter:off
    @JvmField public val GENERIC_9x1: InventoryType = register("generic_9x1", 9, Component.translatable("container.chest"))
    @JvmField public val GENERIC_9x2: InventoryType = register("generic_9x2", 18, GENERIC_9x1.defaultTitle)
    @JvmField public val GENERIC_9x3: InventoryType = register("generic_9x3", 27, GENERIC_9x1.defaultTitle)
    @JvmField public val GENERIC_9x4: InventoryType = register("generic_9x4", 36, Component.translatable("container.chestDouble"))
    @JvmField public val GENERIC_9x5: InventoryType = register("generic_9x5", 45, GENERIC_9x4.defaultTitle)
    @JvmField public val GENERIC_9x6: InventoryType = register("generic_9x6", 54, GENERIC_9x4.defaultTitle)
    @JvmField public val GENERIC_3x3: InventoryType = register("generic_3x3", 9, Component.translatable("container.dispenser"))
    @JvmField public val ANVIL: InventoryType = register("anvil", 3, Component.translatable("container.repair"))
    @JvmField public val BEACON: InventoryType = register("beacon", 1, Component.translatable("container.beacon"))
    @JvmField public val BLAST_FURNACE: InventoryType = register("blast_furnace", 3, Component.translatable("container.blast_furnace"))
    @JvmField public val BREWING_STAND: InventoryType = register("brewing_stand", 5, Component.translatable("container"))
    @JvmField public val CRAFTING_TABLE: InventoryType = register("crafting", 10, Component.translatable("container.crafting"))
    @JvmField public val ENCHANTING_TABLE: InventoryType = register("enchantment", 2, Component.translatable("container.enchant"))
    @JvmField public val FURNACE: InventoryType = register("furnace", 3, Component.translatable("container.furnace"))
    @JvmField public val GRINDSTONE: InventoryType = register("grindstone", 3, Component.translatable("container.grindstone_title"))
    @JvmField public val HOPPER: InventoryType = register("hopper", 5, Component.translatable("container.hopper"))
    @JvmField public val LECTERN: InventoryType = register("lectern", 1, Component.translatable("container.lectern"))
    @JvmField public val LOOM: InventoryType = register("loom", 4, Component.translatable("container.loom"))
    @JvmField public val MERCHANT: InventoryType = register("merchant", 3, Component.translatable("merchant.trades"))
    @JvmField public val SHULKER_BOX: InventoryType = register("shulker_box", 27, Component.translatable("container.shulkerBox"))
    @JvmField public val SMITHING_TABLE: InventoryType = register("smithing", 3, Component.translatable("container.upgrade"))
    @JvmField public val SMOKER: InventoryType = register("smoker", 3, Component.translatable("container.smoker"))
    @JvmField public val CARTOGRAPHY_TABLE: InventoryType = register("cartography_table", 3, Component.translatable("container.cartography_table"))
    @JvmField public val STONECUTTER: InventoryType = register("stonecutter", 2, Component.translatable("container.stonecutter"))

    // @formatter:on
    @JvmStatic
    private fun register(name: String, size: Int, defaultTitle: Component): InventoryType {
        val key = Key.key(name)
        return Registries.INVENTORY_TYPES.register(key, InventoryType.of(key, size, defaultTitle))
    }
}
