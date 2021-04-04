package org.kryptonmc.krypton.inventory

import org.kryptonmc.krypton.api.inventory.Inventory
import org.kryptonmc.krypton.api.inventory.InventoryHolder
import org.kryptonmc.krypton.api.inventory.InventoryType
import org.kryptonmc.krypton.api.inventory.item.ItemStack

abstract class KryptonInventory(
    override val type: InventoryType,
    override val owner: InventoryHolder,
    final override val size: Int
) : Inventory {

    override val items = arrayOfNulls<ItemStack>(size)

    override fun get(index: Int) = items[index]

    override fun set(index: Int, item: ItemStack) {
        items[index] = item
    }

    override fun contains(item: ItemStack) = item in items

    override fun plusAssign(item: ItemStack) = items.forEachIndexed { index, element ->
        if (element == null) {
            items[index] = item
            return
        }
    }

    override fun minusAssign(item: ItemStack) = items.forEachIndexed { index, element ->
        if (element == item) {
            items[index] = null
            return
        }
    }

    override fun clear() = items.forEachIndexed { index, _ -> items[index] = null }

    override fun iterator() = items.iterator()
}