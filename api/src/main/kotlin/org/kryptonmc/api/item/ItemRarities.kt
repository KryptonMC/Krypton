/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla item rarities.
 */
@Catalogue(ItemRarity::class)
public object ItemRarities {

    // @formatter:off
    @JvmField
    public val COMMON: ItemRarity = get("common")
    @JvmField
    public val UNCOMMON: ItemRarity = get("uncommon")
    @JvmField
    public val RARE: ItemRarity = get("rare")
    @JvmField
    public val EPIC: ItemRarity = get("epic")

    // @formatter:on
    @JvmStatic
    private fun get(name: String): ItemRarity = Registries.ITEM_RARITIES.get(Key.key("krypton", name))!!
}
