package org.kryptonmc.krypton.api.world.scoreboard

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.world.scoreboard.criteria.Criteria

/**
 * Represents an objective for a [Scoreboard].
 *
 * @param name the name of the objective
 * @param displayName the display name of the objective (this is what gets displayed on the scoreboard)
 * @param criteria optional criteria for the scoreboard
 * @param renderType how this objective is rendered to the client.
 *
 * @author Callum Seabrook
 */
data class Objective @JvmOverloads constructor(
    val name: String,
    val displayName: Component,
    val criteria: Criteria? = null,
    val renderType: RenderType = RenderType.INTEGER
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