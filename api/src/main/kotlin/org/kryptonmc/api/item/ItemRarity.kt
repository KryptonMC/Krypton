/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

import net.kyori.adventure.text.format.NamedTextColor

/**
 * The rarity of an item. This determines what colour the lore text appears as
 * when the tooltip is read.
 *
 * @property color The colour the lore text will appear.
 */
public enum class ItemRarity(public val color: NamedTextColor) {

    COMMON(NamedTextColor.WHITE),
    UNCOMMON(NamedTextColor.YELLOW),
    RARE(NamedTextColor.AQUA),
    EPIC(NamedTextColor.LIGHT_PURPLE)
}
