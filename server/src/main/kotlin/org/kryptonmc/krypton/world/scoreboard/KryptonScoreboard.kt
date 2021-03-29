package org.kryptonmc.krypton.world.scoreboard

import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.world.scoreboard.*

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