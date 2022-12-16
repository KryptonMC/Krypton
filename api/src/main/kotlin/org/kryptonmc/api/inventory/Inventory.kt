/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.inventory

import org.kryptonmc.api.item.ItemStack

/**
 * An inventory that contains items.
 *
 * The absence of a value in an inventory will always be represented by
 * [ItemStack.empty], not null. This includes the default values in lists of
 * items, such as [items].
 */
public interface Inventory : Iterable<ItemStack> {

    /**
     * The size of this inventory.
     */
    public val size: Int

    /**
     * The type of this inventory.
     */
    public val type: InventoryType

    /**
     * The items in this inventory.
     */
    public val items: List<ItemStack>

    /**
     * If this inventory contains the specified [item].
     *
     * @param item the item
     * @return true if the [item] is in this inventory, false otherwise
     */
    public fun hasItem(item: ItemStack): Boolean

    /**
     * Retrieve an item from this inventory at the specified [index].
     *
     * @param index the index (slot, starts from 0) of the item to retrieve
     * @return the item at that slot, or [ItemStack.empty] if there is no item
     * at that slot
     * @throws ArrayIndexOutOfBoundsException if [index] is out of bounds
     * (not in 0 <= [index] < [size])
     */
    public fun getItem(index: Int): ItemStack

    /**
     * Sets the item at the given [index] to the given [item].
     *
     * @param index the index of the item
     * @param item the new item
     */
    public fun setItem(index: Int, item: ItemStack)

    /**
     * Puts the specified item in the first available empty slot in this
     * inventory, or does nothing if the inventory is full.
     *
     * @param item the item to add
     */
    public fun addItem(item: ItemStack)

    /**
     * Removes the specified [item] from the array, or does nothing if there
     * isn't an element that matches this [item].
     *
     * @param item the item to remove
     */
    public fun removeItem(item: ItemStack)

    /**
     * Clears this inventory.
     */
    public fun clear()
}
