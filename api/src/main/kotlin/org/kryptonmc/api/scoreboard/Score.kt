/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.util.provide

/**
 * Represents the score of a player for a specific [objective].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Score {

    /**
     * The player who's score this is.
     */
    @get:JvmName("player")
    public val player: Player

    /**
     * The objective that's tracking this score.
     */
    @get:JvmName("objective")
    public val objective: Objective

    /**
     * The actual score value.
     */
    @get:JvmName("score")
    public val score: Int

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(player: Player, objective: Objective, score: Int): Score
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new score with the given values.
         *
         * @param player the player being scored
         * @param objective the tracking objective
         * @param score the actual value
         * @return a new score
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(player: Player, objective: Objective, score: Int): Score = FACTORY.of(player, objective, score)
    }
}
