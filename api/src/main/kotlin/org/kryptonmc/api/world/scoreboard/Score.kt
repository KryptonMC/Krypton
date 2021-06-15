/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.scoreboard

import org.kryptonmc.api.entity.player.Player

/**
 * Represents the score of a player for a specific [objective].
 *
 * @param player the player who's score this is
 * @param objective the objective that's tracking this score
 * @param score the actual score value
 */
data class Score(
    val player: Player,
    val objective: Objective,
    val score: Int
)
