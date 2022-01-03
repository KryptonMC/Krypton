/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
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
     * The main inventory area, excluding the hotbar area. This has 27 slots,
     * and covers from slots 9 to 35.
     */
    @get:JvmName("main")
    public val main: List<ItemStack>

    /**
     * The hotbar area. This has 9 slots, and covers from slots 0 to 8.
     */
    @get:JvmName("hotbar")
    public val hotbar: List<ItemStack>

    /**
     * The array of crafting slots in this inventory, where the first 4
     * elements of this array are the input, and the last slot is the output.
     *
     * The returned list is immutable, and of a fixed size.
     */
    @get:JvmName("crafting")
    public val crafting: List<ItemStack>

    /**
     * The array of armor pieces in this inventory.
     *
     * The returned list is immutable, and of a fixed size.
     */
    @get:JvmName("armor")
    public val armor: List<ItemStack>

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
    @get:JvmName("helmet")
    public var helmet: ItemStack

    /**
     * The chestplate this player is currently wearing.
     */
    @get:JvmName("chestplate")
    public var chestplate: ItemStack

    /**
     * The leggings this player is currently wearing.
     */
    @get:JvmName("leggings")
    public var leggings: ItemStack

    /**
     * The boots this player is currently wearing.
     */
    @get:JvmName("boots")
    public var boots: ItemStack

    /**
     * The slot of the currently held item.
     */
    @get:JvmName("heldSlot")
    public val heldSlot: Int

    /**
     * The owner of this player inventory.
     */
    @get:JvmName("owner")
    public val owner: Player

    /**
     * Gets the armour item in the given [slot].
     *
     * @param slot the armour slot
     * @return the armour item in the given [slot]
     */
    public fun armor(slot: ArmorSlot): ItemStack

    /**
     * Sets the armour item in the given [slot] to the give [item].
     *
     * @param slot the armour slot
     * @param item the new item
     */
    public fun setArmor(slot: ArmorSlot, item: ItemStack)

    /**
     * Gets the item the player is holding in the given [hand].
     *
     * @param hand the hand
     * @return the item
     */
    public fun heldItem(hand: Hand): ItemStack

    /**
     * Sets the item the player is holding in the given [hand] to the given
     * [item].
     *
     * @param hand the hand
     * @param item the item
     */
    public fun setHeldItem(hand: Hand, item: ItemStack)
}
