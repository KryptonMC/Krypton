/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla item rarities.
 */
@Catalogue(ItemRarity::class)
public object ItemRarities {

    // @formatter:off
    @JvmField public val COMMON: ItemRarity = register("common", NamedTextColor.WHITE)
    @JvmField public val UNCOMMON: ItemRarity = register("uncommon", NamedTextColor.YELLOW)
    @JvmField public val RARE: ItemRarity = register("rare", NamedTextColor.AQUA)
    @JvmField public val EPIC: ItemRarity = register("epic", NamedTextColor.LIGHT_PURPLE)

    // @formatter:on
    @JvmStatic
    private fun register(name: String, color: TextColor): ItemRarity {
        val key = Key.key("krypton", name)
        return Registries.ITEM_RARITIES.register(key, ItemRarity.of(key, color))
    }
}
