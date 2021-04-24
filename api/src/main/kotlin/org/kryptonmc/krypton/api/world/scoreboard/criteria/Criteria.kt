package org.kryptonmc.krypton.api.world.scoreboard.criteria

/**
 * Criteria for a scoreboard objective to be displayed. Currently unused.
 */
interface Criteria {

    /**
     * The name of this criteria
     */
    val name: String

    /**
     * If this criteria is mutable
     */
    val isMutable: Boolean
}
