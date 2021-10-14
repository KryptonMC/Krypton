/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.text.format.NamedTextColor
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide

/**
 * The slot that a scoreboard may be displayed in.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface DisplaySlot : Keyed {

    /**
     * The team colour associated with this position, or null if this position
     * is not associated with a team colour.
     */
    @get:JvmName("teamColor")
    public val teamColor: NamedTextColor?

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key, color: NamedTextColor?): DisplaySlot

        public fun get(color: NamedTextColor): DisplaySlot?
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new scoreboard position with the given values.
         *
         * @param key the key
         * @param color the associated team colour
         * @return a new scoreboard position
         */
        @JvmStatic
        @JvmOverloads
        @Contract("_ -> new", pure = true)
        public fun of(key: Key, color: NamedTextColor? = null): DisplaySlot = FACTORY.of(key, color)

        /**
         * Gets the scoreboard position for the given team [color], or returns
         * null if there is no scoreboard position associated with the given
         * team [color].
         *
         * @param color the team colour
         * @return the scoreboard position for the colour, or null if there is
         * no position for the colour
         */
        @JvmStatic
        public fun fromTeamColor(color: NamedTextColor): DisplaySlot? = FACTORY.get(color)
    }
}
