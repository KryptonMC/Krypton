/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.data

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.text.format.TextColor
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.provide
import java.awt.Color

/**
 * A colour of a dye.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(DyeColors::class)
public interface DyeColor : Keyed {

    /**
     * The colour of this dye colour.
     */
    @get:JvmName("color")
    public val color: Color

    /**
     * The colour that fireworks will appear.
     */
    @get:JvmName("fireworkColor")
    public val fireworkColor: Color

    /**
     * The colour that this dye colour will appear as when applied to chat
     * messages.
     */
    @get:JvmName("textColor")
    public val textColor: TextColor

    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key, color: Color, fireworkColor: Color, textColor: TextColor): DyeColor
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new dye colour with the given values.
         *
         * @param key the key
         * @param color the base colour
         * @param fireworkColor the colour for fireworks
         * @param textColor the colour for chat messages
         * @return a new dye colour
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(
            key: Key,
            color: Color,
            fireworkColor: Color,
            textColor: TextColor
        ): DyeColor = FACTORY.of(key, color, fireworkColor, textColor)
    }
}
