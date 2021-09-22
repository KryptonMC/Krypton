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
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.item.ItemStack

/**
 * Represents a player's inventory.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface PlayerInventory : Inventory {

    /**
     * The array of crafting slots in this inventory, where the first 4
     * elements of this array are the input, and the last slot is the output.
     */
    @get:JvmName("crafting")
    public val crafting: Array<out ItemStack>

    /**
     * The array of armor pieces in this inventory.
     */
    @get:JvmName("armor")
    public val armor: Array<out ItemStack>

    /**
     * The item that this player is currently holding in their main hand.
     */
    @get:JvmName("mainHand")
    public val mainHand: ItemStack

    /**
     * The item that this player is currently holding in their offhand.
     */
    @get:JvmName("offHand")
    public val offHand: ItemStack

    /**
     * The helmet this player is currently wearing.
     */
    public var helmet: ItemStack

    /**
     * The chestplate this player is currently wearing.
     */
    public var chestplate: ItemStack

    /**
     * The leggings this player is currently wearing.
     */
    public var leggings: ItemStack

    /**
     * The boots this player is currently wearing.
     */
    public var boots: ItemStack

    /**
     * The slot of the currently held item.
     */
    @get:JvmName("heldSlot")
    public val heldSlot: Int

    @get:JvmName("owner")
    override val owner: Player

    /**
     * Gets the armor item in the specified [slot].
     *
     * @param slot the armor slot
     * @return the armor item in the specified [slot]
     */
    public fun armor(slot: ArmorSlot): ItemStack

    /**
     * Sets the armor item in the specified [slot] to the specified [item].
     *
     * @param slot the armor slot
     * @param item the new item
     */
    public fun armor(slot: ArmorSlot, item: ItemStack)

    /**
     * Gets the item the player is holding in the specified [hand].
     *
     * @param hand the hand
     * @return the item held in that hand
     */
    public fun heldItem(hand: Hand): ItemStack
}
