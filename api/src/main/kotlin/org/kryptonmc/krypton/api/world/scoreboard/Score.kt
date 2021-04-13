package org.kryptonmc.krypton.api.world.scoreboard

import org.kryptonmc.krypton.api.entity.entities.Player

/**
 * Represents the score of a player for a specific [objective]
 *
 * @author Callum Seabrook
 */
data class Score(
    val player: Player,
    val objective: Objective,
    val score: Int
)