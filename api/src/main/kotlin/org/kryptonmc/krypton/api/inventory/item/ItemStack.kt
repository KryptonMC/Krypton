package org.kryptonmc.krypton.api.inventory.item

import org.kryptonmc.krypton.api.inventory.item.meta.ItemMeta

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