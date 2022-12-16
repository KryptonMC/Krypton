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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All the built-in inventory types.
 *
 * Note: The player inventory is not included in this list as it is not a valid
 * menu type, and cannot be created, or opened client-side.
 */
@Catalogue(InventoryType::class)
public object InventoryTypes {

    // @formatter:off
    @JvmField
    public val CHEST_ONE_ROW: InventoryType = get("chest_one_row")
    @JvmField
    public val CHEST_TWO_ROWS: InventoryType = get("chest_two_rows")
    @JvmField
    public val CHEST_THREE_ROWS: InventoryType = get("chest_three_rows")
    @JvmField
    public val CHEST_FOUR_ROWS: InventoryType = get("chest_four_rows")
    @JvmField
    public val CHEST_FIVE_ROWS: InventoryType = get("chest_five_rows")
    @JvmField
    public val CHEST_SIX_ROWS: InventoryType = get("chest_six_rows")
    @JvmField
    public val GENERIC_3x3: InventoryType = get("generic_3x3")
    @JvmField
    public val ANVIL: InventoryType = get("anvil")
    @JvmField
    public val BEACON: InventoryType = get("beacon")
    @JvmField
    public val BLAST_FURNACE: InventoryType = get("blast_furnace")
    @JvmField
    public val BREWING_STAND: InventoryType = get("brewing_stand")
    @JvmField
    public val CRAFTING_TABLE: InventoryType = get("crafting")
    @JvmField
    public val ENCHANTING_TABLE: InventoryType = get("enchantment")
    @JvmField
    public val FURNACE: InventoryType = get("furnace")
    @JvmField
    public val GRINDSTONE: InventoryType = get("grindstone")
    @JvmField
    public val HOPPER: InventoryType = get("hopper")
    @JvmField
    public val LECTERN: InventoryType = get("lectern")
    @JvmField
    public val LOOM: InventoryType = get("loom")
    @JvmField
    public val MERCHANT: InventoryType = get("merchant")
    @JvmField
    public val SHULKER_BOX: InventoryType = get("shulker_box")
    @JvmField
    public val SMITHING_TABLE: InventoryType = get("smithing")
    @JvmField
    public val SMOKER: InventoryType = get("smoker")
    @JvmField
    public val CARTOGRAPHY_TABLE: InventoryType = get("cartography_table")
    @JvmField
    public val STONECUTTER: InventoryType = get("stonecutter")

    // @formatter:on
    @JvmStatic
    private fun get(name: String): InventoryType = Registries.INVENTORY_TYPE.get(Key.key(name))!!
}
