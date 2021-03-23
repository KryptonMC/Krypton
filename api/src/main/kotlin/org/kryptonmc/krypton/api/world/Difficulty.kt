package org.kryptonmc.krypton.api.world

import kotlinx.serialization.Serializable

/**
 * Represents the difficulty of a world. That being a measure of how difficult
 * it is to play the game (though this is not always accurate).
 *
 * [id] is the legacy ID of the difficulty. It should mostly only be used
 * internally, as the protocol still uses these legacy IDs.
 *
 * @author Callum Seabrook
 */
@Serializable
enum class Difficulty(val id: Int) {

    /**
     * In peaceful mode, no hostile monsters will spawn in the world.
     * Players will also regain health over time, and the hunger bar
     * does not deplete.
     */
    PEACEFUL(0),

    /**
     * In easy mode, hostile mobs spawn at normal rates, but they deal
     * less damage than on normal difficulty. The hunger bar does
     * deplete, and starving can deal up to 5 hears of damage.
     */
    EASY(1),

    /**
     * In normal mode, hostile mobs spawn at normal rates, and they
     * deal normal amounts of damage. The hunger bar does deplete,
     * and starving can deal up to 9.5 hearts of damage.
     */
    NORMAL(2),

    /**
     * In hard mode, hostile mobs spawn at normal rates, but they deal
     * much greater damage than on normal difficulty. The hunger bar does
     * deplete, and starving can kill players.
     */
    HARD(3);

    companion object {

        private val VALUES = values().associateBy { it.id }

        /**
         * Retrieves a difficulty from its legacy ID. Should only need to be used internally.
         */
        fun fromId(id: Int) = VALUES[id]
    }
}