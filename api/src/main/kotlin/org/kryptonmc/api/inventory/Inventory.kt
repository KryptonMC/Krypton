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
 * An inventory that contains items.
 *
 * The absence of a value in an inventory will always be represented by
 * [ItemStack.empty], not null. This includes the default values in lists of
 * items, such as [items].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Inventory : Iterable<ItemStack> {

    /**
     * The size of this inventory.
     */
    @get:JvmName("size")
    public val size: Int

    /**
     * The type of this inventory.
     */
    @get:JvmName("type")
    public val type: InventoryType

    /**
     * The items in this inventory.
     */
    @get:JvmName("items")
    public val items: List<ItemStack>

    /**
     * Retrieve an item from this inventory at the specified [index].
     *
     * @param index the index (slot, starts from 0) of the item to retrieve
     * @return the item at that slot, or [ItemStack.empty] if there is no item
     * at that slot
     * @throws ArrayIndexOutOfBoundsException if [index] is out of bounds
     * (not in 0 <= [index] < [size])
     */
    public operator fun get(index: Int): ItemStack

    /**
     * If this inventory contains the specified [item].
     *
     * @param item the item
     * @return true if the [item] is in this inventory, false otherwise
     */
    public operator fun contains(item: ItemStack): Boolean
}
