/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

import net.kyori.adventure.key.Keyed
import net.kyori.adventure.text.format.TextColor
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * The rarity of an item. This determines what colour the lore text appears as
 * when the tooltip is read.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface ItemRarity : Keyed {

    /**
     * The colour the lore text will appear.
     */
    @get:JvmName("color")
    public val color: TextColor
}
