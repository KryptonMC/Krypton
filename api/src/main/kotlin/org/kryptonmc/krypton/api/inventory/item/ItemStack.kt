package org.kryptonmc.krypton.api.inventory.item

import org.kryptonmc.krypton.api.inventory.item.meta.ItemMeta

/**
 * Represents a stack of items. Only currently used in inventories.
 *
 * @param type the type of item in this stack
 * @param amount the amount of items in the stack
 * @param meta the meta of this stack
 */
data class ItemStack(
    val type: Material,
    val amount: Int,
    val meta: ItemMeta? = null
) {

    companion object {

        /**
         * An empty item stack.
         */
        val EMPTY = ItemStack(Material.AIR, 0)
    }
}