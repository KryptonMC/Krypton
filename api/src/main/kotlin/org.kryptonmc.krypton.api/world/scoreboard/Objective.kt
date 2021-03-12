package org.kryptonmc.krypton.api.world.scoreboard

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.world.scoreboard.criteria.Criteria

/**
 * Represents an objective for a [Scoreboard]
 *
 * An [Objective] is a goal that teams can work towards
 *
 * @author Callum Seabrook
 */
data class Objective(
    val name: String,
    val displayName: Component,
    val criteria: Criteria,
    val renderType: RenderType
)

/**
 * Controls how an [Objective] is rendered to the client
 *
 * @author Callum Seabrook
 */
enum class RenderType(val id: Int) {

    /**
     * Display an integer value
     */
    INTEGER(0),

    /**
     * Display a number of hearts corresponding to the value
     */
    HEARTS(1)
}