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
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.text.format.TextColor
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide

/**
 * The rarity of an item. This determines what colour the lore text appears as
 * when the tooltip is read.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ItemRarity : Keyed {

    /**
     * The colour the lore text will appear.
     */
    @get:JvmName("color")
    public val color: TextColor

    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key, color: TextColor): ItemRarity
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new item rarity with the given values.
         *
         * @param key the key
         * @param color the colour of the lore text
         * @return a new item rarity
         */
        @JvmStatic
        @Contract("_, _ -> new", pure = true)
        public fun of(key: Key, color: TextColor): ItemRarity = FACTORY.of(key, color)
    }
}
