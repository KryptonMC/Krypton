/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.krypton.api.inventory

import org.kryptonmc.krypton.api.Keyed
import org.kryptonmc.krypton.api.registry.NamespacedKey

/**
 * Represents a type of [Inventory] that holds items.
 *
 * @param size the size of the inventory
 * @param title the default title of the inventory
 * @param key the namespaced key for the inventory
 */
@Suppress("MemberVisibilityCanBePrivate")
enum class InventoryType(
    val size: Int,
    val title: String = "",
    override val key: NamespacedKey = NamespacedKey(value = toString())
) : Keyed {

    /**
     * Built-in inventory types
     */
    ANVIL(3, "Repairing"),
    BEACON(1, "container.beacon"),
    BLAST_FURNACE(3, "Blast Furnace"),
    BREWING_STAND(5, "Brewing"),
    CRAFTING_TABLE(10, "Crafting", NamespacedKey(value = "crafting")),
    ENCHANTING_TABLE(2, "Enchanting", NamespacedKey(value = "enchantment")),
    FURNACE(3, "Furnace"),
    GRINDSTONE(3, "Repair & Disenchant"),
    HOPPER(5, "Item Hopper"),
    LECTERN(1, "Lectern"),
    LOOM(4, "Loom"),
    MERCHANT(3, "Villager"),
    SHULKER_BOX(27, "Shulker Box"),
    SMOKER(3, "Smoker"),
    CARTOGRAPHY_TABLE(3, "Cartography Table", NamespacedKey(value = "cartography")),
    STONECUTTER(2, "Stonecutter"),

    /**
     * Generic inventory types. These have the following naming scheme:
     * GENERIC_columns_rows
     *
     * For example, GENERIC_9_3 is a generic inventory with 9 columns (9 across)
     * and 3 rows (3 down)
     */
    GENERIC_9_1(9),
    GENERIC_9_2(18),
    GENERIC_9_3(27),
    GENERIC_9_4(36),
    GENERIC_9_5(45),
    GENERIC_9_6(54),
    GENERIC_3_3(9),

    /**
     * Miscellaneous types
     *
     * These types cannot be sent to players or constructed
     */
    // Note: the player inventory includes the small 4x4 crafting window and its output slot,
    // hence why the size is 46 rather than 41
    PLAYER(46, "Player");

    override fun toString() = name.lowercase()
}
