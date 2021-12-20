package org.kryptonmc.api.inventory

import org.kryptonmc.api.item.ItemStack

/**
 * An inventory with contents that may be updated.
 */
public interface MutableInventory : Inventory {

    /**
     * Sets the item at the given [index] to the given [item].
     *
     * @param index the index of the item
     * @param item the new item
     */
    public operator fun set(index: Int, item: ItemStack)

    /**
     * Puts the specified item in the first available empty slot in this
     * inventory, or does nothing if the inventory is full.
     *
     * @param item the item to add
     */
    public fun add(item: ItemStack)

    /**
     * Removes the specified [item] from the array, or does nothing if there
     * isn't an element that matches this [item].
     *
     * @param item the item to remove
     */
    public fun remove(item: ItemStack)

    /**
     * Clears this inventory.
     */
    public fun clear()
}
