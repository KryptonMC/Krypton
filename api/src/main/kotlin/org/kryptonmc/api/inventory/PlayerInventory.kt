/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.inventory

import org.kryptonmc.api.entity.ArmorSlot
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.item.ItemStack

/**
 * Represents a player's inventory.
 */
public interface PlayerInventory : Inventory {

    /**
     * The main inventory area, excluding the hotbar area. This has 27 slots,
     * and covers from slots 9 to 35.
     */
    public val main: List<ItemStack>

    /**
     * The hotbar area. This has 9 slots, and covers from slots 0 to 8.
     */
    public val hotbar: List<ItemStack>

    /**
     * The array of crafting slots in this inventory, where the first 4
     * elements of this array are the input, and the last slot is the output.
     *
     * The returned list is immutable, and of a fixed size.
     */
    public val crafting: List<ItemStack>

    /**
     * The array of armor pieces in this inventory.
     *
     * The returned list is immutable, and of a fixed size.
     */
    public val armor: List<ItemStack>

    /**
     * The item that this player is currently holding in their main hand.
     */
    public val mainHand: ItemStack

    /**
     * The item that this player is currently holding in their offhand.
     */
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
    public val heldSlot: Int

    /**
     * The owner of this player inventory.
     */
    public val owner: Player

    /**
     * Gets the armour item in the given [slot].
     *
     * @param slot the armour slot
     * @return the armour item in the given [slot]
     */
    public fun getArmor(slot: ArmorSlot): ItemStack

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
    public fun getHeldItem(hand: Hand): ItemStack

    /**
     * Sets the item the player is holding in the given [hand] to the given
     * [item].
     *
     * @param hand the hand
     * @param item the item
     */
    public fun setHeldItem(hand: Hand, item: ItemStack)
}
