/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.inventory

import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.inventory.item.ItemStack

/**
 * Represents a player's inventory
 *
 * This should follow the standard inventory of the protocol, which documents the following:
 * - The first slot, 0, is the crafting output slot
 * - Slots 1-4 are the crafting input slots
 * - Slots 5-8 are the armor slots (helmet, then chestplate, then leggings, then boots)
 * - Slots 9-35 are the main inventory (27 slots)
 * - Slots 36-44 are the player's hotbar (9 slots)
 * - Slot 45 is the offhand slot
 */
interface PlayerInventory : Inventory {

    /**
     * The crafting inventory (sub selection of [items] containing the first 5 elements (0-4))
     *
     * May copy the [items] array
     */
    val crafting: Array<ItemStack?>

    /**
     * The elements of this inventory which are armour pieces
     *
     * May copy the [items] array
     */
    val armor: Array<ItemStack?>

    /**
     * The main inventory (sub selection of [items] containing elements 9-35 inclusive)
     *
     * May copy the [items] array
     */
    val main: Array<ItemStack?>

    /**
     * The hotbar (sub selection of [items] containing elements 36-44)
     *
     * May copy the [items] array
     */
    val hotbar: Array<ItemStack?>

    /**
     * The item that this player is currently holding in their main hand.
     *
     * Will be an [ItemStack] of type AIR and amount 0 if this player is
     * not holding anything in their main hand.
     */
    val mainHand: ItemStack?

    /**
     * The item that this player is currently holding in their offhand.
     *
     * Will be an [ItemStack] of type AIR and amount 0 if this player is
     * not holding anything in their offhand.
     */
    val offHand: ItemStack?

    /**
     * The helmet this player is currently wearing, or null if this player
     * isn't wearing a helmet
     */
    val helmet: ItemStack?

    /**
     * The chestplate this player is currently wearing, or null if this
     * player isn't wearing a chestplate
     */
    val chestplate: ItemStack?

    /**
     * The leggings this player is currently wearing, or null if this
     * player isn't wearing any leggings
     */
    val leggings: ItemStack?

    /**
     * The boots this player is currently wearing, or null if this player
     * isn't wearing any boots
     */
    val boots: ItemStack?

    /**
     * Gets the slot of the currently held item
     */
    val heldSlot: Int

    override val owner: Player
}
