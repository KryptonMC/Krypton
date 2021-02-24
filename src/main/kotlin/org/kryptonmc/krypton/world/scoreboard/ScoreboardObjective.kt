package org.kryptonmc.krypton.world.scoreboard

import me.bardy.komponent.Component

data class ScoreboardObjective(
    val name: String,
    val value: Component?,
    val type: ObjectiveType
)

enum class ObjectiveAction(val id: Int) {

    CREATE_SCOREBOARD(0),
    REMOVE_SCOREBOARD(1),
    UPDATE_DISPLAY_TEXT(2)
}

enum class ObjectiveType(val id: Int) {

    INTEGER(0),
    HEARTS(1)
}