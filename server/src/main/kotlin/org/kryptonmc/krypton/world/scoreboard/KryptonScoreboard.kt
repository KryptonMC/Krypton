package org.kryptonmc.krypton.world.scoreboard

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.world.scoreboard.*
import org.kryptonmc.krypton.api.world.scoreboard.criteria.Criterion

data class KryptonScoreboard(
    override val position: ScoreboardPosition,
    override val name: String,
    override val objectives: List<Objective> = listOf(),
    override val teams: List<Team> = listOf(),
    override val scores: Map<Player, Score> = mapOf(),
    override val players: Set<Player> = setOf()
) : Scoreboard {

    override fun registerObjective(objective: Objective): Objective = Objective(
        "",
        Component.empty(),
        Criterion.AIR,
        RenderType.INTEGER
    )

    override fun team(name: String) = teams.firstOrNull { it.name == name }

    // TODO: Make this work as intended
    @Suppress("ImplicitNullableNothingType")
    override fun playerTeam(player: Player) = null

    // TODO: Make this work as intended
    override fun resetScores(player: Player) = Unit
}