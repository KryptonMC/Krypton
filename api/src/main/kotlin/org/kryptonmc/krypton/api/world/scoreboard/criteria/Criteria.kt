package org.kryptonmc.krypton.api.world.scoreboard.criteria

/**
 * Criteria for a scoreboard objective to be displayed. Currently unused.
 *
 * @author Callum Seabrook
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