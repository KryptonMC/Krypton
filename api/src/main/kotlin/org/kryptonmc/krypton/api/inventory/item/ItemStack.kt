package org.kryptonmc.krypton.api.inventory.item

import org.kryptonmc.krypton.api.inventory.item.meta.ItemMeta

data class ItemStack(
    val type: ItemType,
    val amount: Int,
    val meta: ItemMeta? = null
)