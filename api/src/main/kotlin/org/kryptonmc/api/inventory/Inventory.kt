/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.inventory

import org.kryptonmc.api.item.ItemStack

/**
 * Represents an inventory that holds [ItemStack]s
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface Inventory : Iterable<ItemStack> {

    /**
     * The size of this inventory
     */
    val size: Int

    /**
     * The holder that owns this inventory
     */
    val owner: InventoryHolder

    /**
     * The type of this inventory
     */
    val type: InventoryType

    /**
     * The items in this inventory.
     */
    val items: Array<out ItemStack>

    /**
     * Retrieve an item from this inventory at the specified [index]
     *
     * @param index the index (slot, starts from 0) of the item to retrieve
     * @return the item at that slot, or [ItemStack.EMPTY] if there is no item
     * at that slot
     * @throws ArrayIndexOutOfBoundsException if [index] is out of bounds
     * (not in 0 <= [index] < [size])
     */
    operator fun get(index: Int): ItemStack

    /**
     * Set the item at the specified [index] to the specified [item]
     *
     * Will replace the item if there is already an item in that slot
     *
     * @param index the index to put the item
     * @param item the item to add
     * @throws ArrayIndexOutOfBoundsException if [index] is out of bounds
     * (not in 0 <= [index] < [size])
     */
    @JvmName("put")
    operator fun set(index: Int, item: ItemStack)

    /**
     * Puts the specified item in the first available empty slot in this inventory, or
     * does nothing if the inventory is full.
     *
     * @param item the item to add
     */
    @JvmName("add")
    operator fun plusAssign(item: ItemStack)

    /**
     * Removes the specified [item] from the array, or does nothing if there isn't an
     * element that matches this [item]
     *
     * @param item the item to remove
     */
    @JvmName("remove")
    operator fun minusAssign(item: ItemStack)

    /**
     * If this inventory contains the specified [item]
     *
     * @param item the item
     * @return true if the [item] is in this inventory, false otherwise
     */
    operator fun contains(item: ItemStack): Boolean

    /**
     * Clears this inventory
     */
    fun clear()
}
