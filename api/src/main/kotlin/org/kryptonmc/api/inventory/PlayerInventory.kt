/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.inventory

import org.kryptonmc.api.entity.ArmorSlot
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.item.ItemStack

/**
 * Represents a player's inventory.
 */
interface PlayerInventory : Inventory {

    /**
     * The array of crafting slots in this inventory, where the first
     * 4 elements of this array are the input, and the last slot is
     * the output.
     */
    val crafting: Array<out ItemStack>

    /**
     * The array of armor pieces in this inventory.
     */
    val armor: Array<out ItemStack>

    /**
     * The item that this player is currently holding in their main hand.
     *
     * Will return [ItemStack.empty] if the player is not holding anything
     * in their main hand.
     */
    val mainHand: ItemStack

    /**
     * The item that this player is currently holding in their offhand.
     *
     * Will return [ItemStack.empty] if the player is not holding anything
     * in their off hand.
     */
    val offHand: ItemStack

    /**
     * The helmet this player is currently wearing, or [ItemStack.empty]
     * if this player isn't wearing a helmet
     */
    val helmet: ItemStack

    /**
     * The chestplate this player is currently wearing, or [ItemStack.empty]
     * if this player isn't wearing a chestplate
     */
    val chestplate: ItemStack

    /**
     * The leggings this player is currently wearing, or [ItemStack.empty]
     * if this player isn't wearing any leggings
     */
    val leggings: ItemStack

    /**
     * The boots this player is currently wearing, or [ItemStack.empty] if
     * this player isn't wearing any boots
     */
    val boots: ItemStack

    /**
     * The slot of the currently held item
     */
    val heldSlot: Int

    override val owner: Player

    /**
     * Gets the armor item in the specified [slot].
     *
     * @return the armor item in the specified [slot]
     */
    fun armor(slot: ArmorSlot): ItemStack
}
