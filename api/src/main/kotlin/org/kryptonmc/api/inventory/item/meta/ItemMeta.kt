/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.inventory.item.meta

import net.kyori.adventure.text.Component

/**
 * Holder for metadata values for an item
 */
interface ItemMeta {

    /**
     * The display name of the item, or null if this item doesn't
     * have a display name
     */
    val displayName: Component?

    /**
     * The lore of the item. Will be empty if this item doesn't
     * have any lore
     */
    val lore: List<Component>
}
