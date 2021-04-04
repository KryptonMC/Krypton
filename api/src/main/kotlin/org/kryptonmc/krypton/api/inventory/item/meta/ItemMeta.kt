package org.kryptonmc.krypton.api.inventory.item.meta

import net.kyori.adventure.text.Component

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