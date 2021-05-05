/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
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
        @JvmField
        val EMPTY = ItemStack(Material.AIR, 0)
    }
}
