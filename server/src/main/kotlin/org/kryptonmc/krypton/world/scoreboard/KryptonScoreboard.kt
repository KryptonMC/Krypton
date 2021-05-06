/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.scoreboard

import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.world.scoreboard.Objective
import org.kryptonmc.krypton.api.world.scoreboard.Score
import org.kryptonmc.krypton.api.world.scoreboard.Scoreboard
import org.kryptonmc.krypton.api.world.scoreboard.ScoreboardPosition
import org.kryptonmc.krypton.api.world.scoreboard.Team

data class KryptonScoreboard(
    override val position: ScoreboardPosition,
    override val name: String,
    override val objectives: MutableList<Objective> = mutableListOf(),
    override val teams: MutableList<Team> = mutableListOf(),
    override val scores: MutableMap<Player, Score> = mutableMapOf(),
    override val players: MutableSet<Player> = mutableSetOf()
) : Scoreboard {

    override fun registerObjective(objective: Objective): Objective {
        objectives += objective
        return objective
    }

    override fun team(name: String) = teams.firstOrNull { it.name == name }

    override fun playerTeam(player: Player) = teams.firstOrNull { player in it.members }

    override fun resetScores(player: Player) = scores.clear()
}
