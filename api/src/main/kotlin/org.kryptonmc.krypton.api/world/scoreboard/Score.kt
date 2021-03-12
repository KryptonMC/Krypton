package org.kryptonmc.krypton.api.world.scoreboard

import org.kryptonmc.krypton.api.entity.entities.Player

data class Score(
    val player: Player,
    val objective: Objective,
    val score: Int
)