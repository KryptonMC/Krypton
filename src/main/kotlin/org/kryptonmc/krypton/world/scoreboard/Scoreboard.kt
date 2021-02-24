package org.kryptonmc.krypton.world.scoreboard

data class Scoreboard(
    val position: ScoreboardPosition,
    val name: String,
    val objectives: List<ScoreboardObjective>
)

enum class ScoreboardPosition(val id: Int) {

    LIST(0),
    SIDEBAR(1),
    BELOW_NAME(2),
    TEAM_SPECIFIC(-1) // Team-specific position in the range 3-18, indexed as 3 + team colour
}