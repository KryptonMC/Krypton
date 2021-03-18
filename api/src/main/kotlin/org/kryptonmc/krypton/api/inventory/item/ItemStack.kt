package org.kryptonmc.krypton.api.inventory.item

data class ItemStack(
    val type: ItemType,
    val amount: Int
)